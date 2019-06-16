package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Reposts {
    @SerializedName("count")
    private int count;

    public Reposts() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
