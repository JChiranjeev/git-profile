package dev.jainchiranjeev.gitprofile.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dev.jainchiranjeev.gitprofile.models.GitFollowersModel;
import dev.jainchiranjeev.gitprofile.models.GitFollowingModel;
import dev.jainchiranjeev.gitprofile.models.GitProfileModel;
import dev.jainchiranjeev.gitprofile.models.GitRepoModel;

public class JSONParser {
    public GitProfileModel getProfileFromJson(String json) {
//        Map json data to GitProfileModel object
        GitProfileModel model = new GitProfileModel();
        try {
            JSONObject jsonObject = new JSONObject(json);
            model.login = jsonObject.getString("login");
            model.id = jsonObject.getString("id");
            model.nodeId = jsonObject.getString("node_id");
            model.avatarUrl = getUrlFromString(jsonObject.getString("avatar_url"));
            model.gravatarId = jsonObject.getString("gravatar_id");
            model.url = getUrlFromString(jsonObject.getString("url"));
            model.htmlUrl = getUrlFromString(jsonObject.getString("html_url"));
            model.followersUrl = getUrlFromString(jsonObject.getString("followers_url"));
            model.followingUrl = getUrlFromString(jsonObject.getString("following_url"));
            model.gistsUrl = getUrlFromString(jsonObject.getString("gists_url"));
            model.starredUrl = getUrlFromString(jsonObject.getString("starred_url"));
            model.subscriptionsUrl = getUrlFromString(jsonObject.getString("subscriptions_url"));
            model.organizationsUrl = getUrlFromString(jsonObject.getString("organizations_url"));
            model.reposUrl = getUrlFromString(jsonObject.getString("repos_url"));
            model.eventsUrl = getUrlFromString(jsonObject.getString("events_url"));
            model.receivedEventsUrl = getUrlFromString(jsonObject.getString("received_events_url"));
            model.type = jsonObject.getString("type");
            model.siteAdmin = Boolean.valueOf(jsonObject.getString("site_admin"));
            model.name = jsonObject.getString("name");
            model.company = jsonObject.getString("company");
            model.blog = getUrlFromString(jsonObject.getString("blog"));
            model.location = jsonObject.getString("location");
            model.email = jsonObject.getString("email");
            model.hireable = Boolean.valueOf(jsonObject.getString("hireable"));
            model.bio = jsonObject.getString("bio");
            model.publicRepos = jsonObject.getInt("public_repos");
            model.publicGists = jsonObject.getInt("public_gists");
            model.followers = jsonObject.getInt("followers");
            model.following = jsonObject.getInt("following");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            model.createdAt = format.parse(jsonObject.getString("created_at"));
            model.updatedAt = format.parse(jsonObject.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return model;
    }

    //    Method to get urls from string. Returns null if url string empty
    private URL getUrlFromString(String urlString) throws MalformedURLException {
        URL url;
        if (urlString == null || urlString.length() == 0) {
            return null;
        } else {
            url = new URL(urlString);
            return url;
        }
    }

    //    Map json data to GitSubsModel for graph display
    public List<GitRepoModel> getReposFromJson(String json) {
        List<GitRepoModel> modelList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            JSONArray jsonArray = new JSONArray(json);
//            Iterate over jsonArray and store data in List of GitRepoModel
            for (int i = 0; i < jsonArray.length(); i++) {
                GitRepoModel model = new GitRepoModel();
                JSONObject obj = jsonArray.getJSONObject(i);
                model.language = obj.getString("language");
                model.stargazersCount = Integer.valueOf(obj.getString("stargazers_count"));
                model.name = obj.getString("name");
                model.description = obj.getString("description");
                model.html_url = obj.getString("html_url");
                model.createdAt = format.parse(obj.getString("created_at"));
                model.updatedAt = format.parse(obj.getString("updated_at"));
                model.pushedAt = format.parse(obj.getString("pushed_at"));
                model.clone_url = obj.getString("clone_url");
                model.watchersCount = Integer.valueOf(obj.getString("watchers_count"));
                if (!obj.getString("license").equals("null")) {
                    JSONObject licenseObj = obj.getJSONObject("license");
                    model.licenseName = licenseObj.getString("name");
                } else {
                    model.licenseName = null;
                }
                modelList.add(model);
            }
            System.out.println(modelList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return modelList;
    }

    public List<GitFollowersModel> getFollowersFromJson(String json) {
        List<GitFollowersModel> followersList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                GitFollowersModel model = new GitFollowersModel();
                JSONObject obj = jsonArray.getJSONObject(i);
                model.login = obj.getString("login");
                model.avatarUrl = obj.getString("avatar_url");
                model.profileUrl = obj.getString("html_url");
                followersList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return followersList;
    }

    public List<GitFollowingModel> getFollowingFromJson(String json) {
        List<GitFollowingModel> followingList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                GitFollowingModel model = new GitFollowingModel();
                JSONObject obj = jsonArray.getJSONObject(i);
                model.login = obj.getString("login");
                model.avatarUrl = obj.getString("avatar_url");
                model.profileUrl = obj.getString("html_url");
                followingList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return followingList;
    }
}
