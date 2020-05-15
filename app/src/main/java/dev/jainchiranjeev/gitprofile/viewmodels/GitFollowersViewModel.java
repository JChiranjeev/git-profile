package dev.jainchiranjeev.gitprofile.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import dev.jainchiranjeev.gitprofile.utils.URLUtils;

public class GitFollowersViewModel extends AndroidViewModel {
    GitFollowersLiveData gitFollowersLiveData;
    public GitFollowersViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getGitFollowers(Context context, String username, Boolean followers) {
        gitFollowersLiveData = new GitFollowersLiveData(context, username, followers);
        return gitFollowersLiveData;
    }
}

class GitFollowersLiveData extends LiveData<String> {
    private final Context context;
    private final Boolean followers;

    GitFollowersLiveData(Context context, String username, Boolean followers) {
        this.context = context;
        this.followers = followers;
        getFollowers(username);
    }

    private void getFollowers(String username) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String username = strings[0];
                StringBuffer responseJson = new StringBuffer("");
                URL gitApiBaseUrl = null;
                URL gitFollowersUrl = null;
                BufferedReader in = null;
                try {
                    gitApiBaseUrl = new URL(URLUtils.gitApiBaseUrl);
                    if(followers) {
                        gitFollowersUrl = new URL(gitApiBaseUrl, username + "/followers");
                    } else {
                        gitFollowersUrl = new URL(gitApiBaseUrl, username + "/following");
                    }

                    HttpsURLConnection connection = (HttpsURLConnection) gitFollowersUrl.openConnection();
                    connection.setRequestMethod("GET");

                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while((line = in.readLine()) != null) {
                            responseJson.append(line);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return responseJson.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                setValue(s);
            }
        }.execute(username);
    }
}
