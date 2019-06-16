package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Link extends AttachmentPhoto {
    @SerializedName("url")
    private String url;

    @SerializedName("title")
    private String title;

    @SerializedName("photo")
    private Photo photo;

    public Link() {
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
