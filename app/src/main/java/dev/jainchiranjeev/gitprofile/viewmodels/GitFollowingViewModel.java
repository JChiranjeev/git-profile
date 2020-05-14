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

public class GitFollowingViewModel extends AndroidViewModel {
    GitFollowingLiveData gitFollowingLiveData;

    public GitFollowingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getGitFollowing(Context context, String username) {
        gitFollowingLiveData = new GitFollowingLiveData(context, username);
        return gitFollowingLiveData;
    }
}

class GitFollowingLiveData extends LiveData<String> {
    private final Context context;

    GitFollowingLiveData(Context context, String username) {
        this.context = context;
        getFollowing(username);
    }

    private void getFollowing(String username) {
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
                    gitFollowersUrl = new URL(gitApiBaseUrl, username + "/following");

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