package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Size implements Serializable {
    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
