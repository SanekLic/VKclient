package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Reposts {
    @SerializedName("count")
    private int count;
    @SerializedName("user_reposted")
    private Boolean userReposted;

    public Reposts() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getUserReposted() {
        return userReposted;
    }

    public void setUserReposted(Boolean userReposted) {
        this.userReposted = userReposted;
    }
}
