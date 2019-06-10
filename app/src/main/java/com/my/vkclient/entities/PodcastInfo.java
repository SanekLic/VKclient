package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class PodcastInfo {
    @SerializedName("cover")
    private Photo cover;

    public Photo getCover() {
        return cover;
    }
}
