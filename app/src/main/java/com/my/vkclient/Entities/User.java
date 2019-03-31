package com.my.vkclient.Entities;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String photo_max_orig;
    private CropPhoto crop_photo;

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto_max_orig() {
        return photo_max_orig;
    }

    public CropPhoto getCrop_photo() {
        return crop_photo;
    }
}
