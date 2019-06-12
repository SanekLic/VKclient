package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Comments {
    @SerializedName("count")
    private int count;

    public Comments() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
