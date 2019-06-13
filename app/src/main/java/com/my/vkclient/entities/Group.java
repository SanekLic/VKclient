package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo_100")
    private String photo100Url;

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto100Url() {
        return photo100Url;
    }

    public void setPhoto100Url(String photo100Url) {
        this.photo100Url = photo100Url;
    }
}
