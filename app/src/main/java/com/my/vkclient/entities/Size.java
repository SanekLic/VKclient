package com.my.vkclient.entities;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class Size {
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;
    @SerializedName("src")
    private String src;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    protected Size(Parcel in) {
        type = in.readString();
        url = in.readString();
        src = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getSrc() {
        return src;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
