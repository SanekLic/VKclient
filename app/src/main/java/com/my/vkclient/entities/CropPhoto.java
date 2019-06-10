package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class CropPhoto {
    @SerializedName("photo")
    private Photo photo;
    @SerializedName("rect")
    private Rect rect;
    @SerializedName("crop")
    private Rect crop;

    private String cropPhotoUrl;
    private int cropPhotoWidth;
    private int cropPhotoHeight;
    private float cropRectX;
    private float cropRectY;
    private float cropRectX2;
    private float cropRectY2;

    public CropPhoto() {
    }

    public int getCropPhotoWidth() {
        return cropPhotoWidth;
    }

    public void setCropPhotoWidth(int cropPhotoWidth) {
        this.cropPhotoWidth = cropPhotoWidth;
    }

    public int getCropPhotoHeight() {
        return cropPhotoHeight;
    }

    public void setCropPhotoHeight(int cropPhotoHeight) {
        this.cropPhotoHeight = cropPhotoHeight;
    }

    public String getCropPhotoUrl() {
        return cropPhotoUrl;
    }

    public void setCropPhotoUrl(String cropPhotoUrl) {
        this.cropPhotoUrl = cropPhotoUrl;
    }

    public float getCropRectX() {
        return cropRectX;
    }

    public void setCropRectX(float cropRectX) {
        this.cropRectX = cropRectX;
    }

    public float getCropRectY() {
        return cropRectY;
    }

    public void setCropRectY(float cropRectY) {
        this.cropRectY = cropRectY;
    }

    public float getCropRectX2() {
        return cropRectX2;
    }

    public void setCropRectX2(float cropRectX2) {
        this.cropRectX2 = cropRectX2;
    }

    public float getCropRectY2() {
        return cropRectY2;
    }

    public void setCropRectY2(float cropRectY2) {
        this.cropRectY2 = cropRectY2;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Rect getCrop() {
        return crop;
    }

    public void setCrop(Rect crop) {
        this.crop = crop;
    }
}
