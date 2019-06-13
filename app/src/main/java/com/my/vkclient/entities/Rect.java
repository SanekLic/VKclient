package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Rect {
    @SerializedName("x")
    private float x;

    @SerializedName("y")
    private float y;

    @SerializedName("x2")
    private float x2;

    @SerializedName("y2")
    private float y2;

    public Rect(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

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
