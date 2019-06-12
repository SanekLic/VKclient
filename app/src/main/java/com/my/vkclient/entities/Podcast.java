package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Podcast extends AttachmentPhoto {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("podcast_info")
    private PodcastInfo podcastInfo;

    public Podcast() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PodcastInfo getPodcastInfo() {
        return podcastInfo;
    }
}
