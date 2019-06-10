package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Comments {
    @SerializedName("count")
    private int count;
    @SerializedName("can_post")
    private Boolean canPost;

    public Comments() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getCanPost() {
        return canPost;
    }

    public void setCanPost(Boolean canPost) {
        this.canPost = canPost;
    }
}
