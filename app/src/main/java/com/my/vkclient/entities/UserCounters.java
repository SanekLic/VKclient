package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class UserCounters {
    @SerializedName("friends")
    private int friends;

    @SerializedName("photos")
    private int photos;

    public UserCounters() {
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }
}
