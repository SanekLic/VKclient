package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo extends AttachmentPhoto {
    @SerializedName("id")
    private int id;
    @SerializedName("sizes")
    private List<Size> sizes;

    public Photo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }
}
