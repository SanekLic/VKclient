package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rect implements Serializable {
    @SerializedName("x")
    private float x;

    @SerializedName("y")
    private float y;

    @SerializedName("x2")
    private float x2;

    @SerializedName("y2")
    private float y2;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }
}
