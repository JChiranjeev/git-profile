package dev.jainchiranjeev.gitprofile.models;

import androidx.annotation.NonNull;

import java.net.URL;
import java.util.Date;

public class GitProfileModel {
    public Integer publicRepos, publicGists, followers, following;
    public String login, id, nodeId, gravatarId, type, name, company, location, email, bio;
    public URL avatarUrl, url, htmlUrl, followersUrl, followingUrl, gistsUrl, starredUrl, subscriptionsUrl, organizationsUrl, reposUrl, eventsUrl, receivedEventsUrl, blog;
    public Boolean siteAdmin, hireable;
    public Date createdAt, updatedAt;
}
