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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dev.jainchiranjeev.gitprofile.models.GitRepoModel;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;
import dev.jainchiranjeev.gitprofile.utils.URLUtils;

public class GitReposViewModel extends AndroidViewModel {
    GitReposLiveData gitReposLiveData;
    public GitReposViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getGitReposList(Context context, String username) {
        gitReposLiveData = new GitReposLiveData(context, username);
        return gitReposLiveData;
    }
}

class GitReposLiveData extends MutableLiveData<String> {
    private final Context context;
    GitReposLiveData(Context context, String username) {
        this.context = context;
        loadData(username);
    }

    @Override
    public void postValue(String value) {
        super.postValue(value);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    //    API Call
    private void loadData(String username) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String username = strings[0];
                URL gitApiBaseUrl = null;
                URL gitReposUrl = null;
                List<GitRepoModel> modelList = null;
                BufferedReader in = null;
                StringBuffer responseJson = null;
                try {
                    gitApiBaseUrl = new URL(URLUtils.gitApiBaseUrl);
                    gitReposUrl = new URL(gitApiBaseUrl, username + "/repos");

//                    Open Http Connection
                    HttpsURLConnection connection = (HttpsURLConnection) gitReposUrl.openConnection();
                    connection.setRequestMethod("GET");

//                    Get InputStream for Incoming Data
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        responseJson = new StringBuffer();
                        String line;
                        while((line = in.readLine()) != null) {
                            responseJson.append(line);
                        }
                    }
                } catch (MalformedURLException e) {
//                    TODO: Throw Custom Exceptions
                    e.printStackTrace();
                } catch (IOException e) {
//                    TODO: Throw Custom Exceptions
                    e.printStackTrace();
                }
//                return new JSONParser().getReposFromJson(responseJson.toString());
                return responseJson.toString();
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                setValue(response);
            }
        }.execute(username);
    }
}