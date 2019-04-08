package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CropPhoto implements Serializable {
    @SerializedName("photo")
    private Photo photo;

    @SerializedName("rect")
    private Rect rect;

    @SerializedName("crop")
    private Rect crop;

    public Photo getPhoto() {
        return photo;
    }

    public Rect getRect() {
        return rect;
    }

    public Rect getCrop() {
        return crop;
    }
}
