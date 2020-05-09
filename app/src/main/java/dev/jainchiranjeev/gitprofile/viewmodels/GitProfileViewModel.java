package dev.jainchiranjeev.gitprofile.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import dev.jainchiranjeev.gitprofile.models.GitProfileModel;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;
import dev.jainchiranjeev.gitprofile.utils.URLUtils;

public class GitProfileViewModel extends AndroidViewModel {
    GitProfileLiveData gitProfileLiveData;
    public GitProfileViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<GitProfileModel> getGitProfile(Context context, String username) {
        gitProfileLiveData = new GitProfileLiveData(context, username);
        return gitProfileLiveData;
    }
}

class GitProfileLiveData extends MutableLiveData<GitProfileModel> {
    private final Context context;

    GitProfileLiveData(Context context, String username) {
        this.context = context;
        loadData(username);
    }

    @Override
    public void postValue(GitProfileModel value) {
        super.postValue(value);
    }

    @Override
    public void setValue(GitProfileModel value) {
        super.setValue(value);
    }

//    API Call
    private void loadData(String username) {
        new AsyncTask<String,Void,GitProfileModel>() {
            @Override
            protected GitProfileModel doInBackground(String... strings) {
                String username = strings[0];
                URL gitApiBaseUrl = null;
                URL gitProfileUrl = null;
                GitProfileModel gitProfile = null;
                BufferedReader in = null;
                StringBuffer responseJson = null;
                try {
                    gitApiBaseUrl = new URL(URLUtils.gitApiBaseUrl);
                    gitProfileUrl = new URL(gitApiBaseUrl, username);

//                    Open Http Connection
                    HttpsURLConnection connection = (HttpsURLConnection) gitProfileUrl.openConnection();
                    connection.setRequestMethod("GET");

//                    Get Input Stream for Incoming Data
                    if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        responseJson = new StringBuffer();
                        String line;
                        while((line = in.readLine()) != null) {
                            responseJson.append(line);
                        }
                        return new JSONParser().getProfileFromJson(responseJson.toString());
                    } else if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        return null;
                    }
                } catch (MalformedURLException e) {
//                    TODO: Throw Custom Exceptions
                    e.printStackTrace();
                } catch (IOException e) {
//                    TODO: Throw Custom Exceptions
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(GitProfileModel gitProfileModel) {
                super.onPostExecute(gitProfileModel);
                setValue(gitProfileModel);
            }
        }.execute(username);
    }
}
