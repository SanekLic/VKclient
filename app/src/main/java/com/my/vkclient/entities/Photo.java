package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("sizes")
    private List<Size> sizes;

    public int getId() {
        return id;
    }

    public List<Size> getSizes() {
        return sizes;
    }
}
