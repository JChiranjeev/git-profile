package dev.jainchiranjeev.gitprofile.models;

import java.util.Date;
import java.util.HashMap;

public class GitRepoModel {
    public String language,name,description, html_url, clone_url, licenseName;
    public Integer stargazersCount, watchersCount;
    public Date createdAt, updatedAt, pushedAt;
}
