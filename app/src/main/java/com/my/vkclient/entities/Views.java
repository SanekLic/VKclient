package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Views {
    @SerializedName("count")
    private int count;

    public Views() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
