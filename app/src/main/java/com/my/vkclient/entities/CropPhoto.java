package com.my.vkclient.entities;

public class CropPhoto {
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
