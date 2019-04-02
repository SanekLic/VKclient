package com.my.vkclient.entities;

import java.io.Serializable;

public class CropPhoto implements Serializable {
    private Photo photo;
    private Rect rect;
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
