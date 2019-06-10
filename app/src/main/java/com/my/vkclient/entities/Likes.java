package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Likes {
    @SerializedName("count")
    private int count;
    @SerializedName("user_likes")
    private Boolean userLikes;
    @SerializedName("can_like")
    private Boolean canLike;
    @SerializedName("can_publish")
    private Boolean canPublish;

    public Likes() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Boolean userLikes) {
        this.userLikes = userLikes;
    }

    public Boolean getCanLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public Boolean getCanPublish() {
        return canPublish;
    }

    public void setCanPublish(Boolean canPublish) {
        this.canPublish = canPublish;
    }
}
