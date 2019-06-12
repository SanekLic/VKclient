package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Video extends AttachmentPhoto {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("photo_320")
    private String photo320Url;
    @SerializedName("photo_640")
    private String photo640Url;
    @SerializedName("photo_800")
    private String photo800Url;

    public Video() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto320Url() {
        return photo320Url;
    }

    public String getPhoto640Url() {
        return photo640Url;
    }

    public String getPhoto800Url() {
        return photo800Url;
    }
}
