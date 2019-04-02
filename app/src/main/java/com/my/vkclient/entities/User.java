package com.my.vkclient.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private int id;
    private int online;
    private String first_name;
    private String last_name;
    private String photo_max_orig;
    private String photo_100;
    private CropPhoto crop_photo;

    public int getId() {
        return id;
    }

    public int getOnline() {
        return online;
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

    public String getPhoto_100() {
        return photo_100;
    }

    public CropPhoto getCrop_photo() {
        return crop_photo;
    }

    public boolean equals(User object) {
        if (this == object) return true;
        return compare(object).isEmpty();
    }

    public ArrayList<UserDifferences> compare(User object) {
        ArrayList<UserDifferences> differences = new ArrayList<>();

        if (getId() != object.getId()) {
            differences.add(UserDifferences.DIFFERENT_ID);
        }

        if (getOnline() != object.getOnline()) {
            differences.add(UserDifferences.DIFFERENT_ONLINE);
        }

        if (!getFirst_name().equals(object.getFirst_name())) {
            differences.add(UserDifferences.DIFFERENT_FIRST_NAME);
        }

        if (!getLast_name().equals(object.getLast_name())) {
            differences.add(UserDifferences.DIFFERENT_LAST_NAME);
        }

        if (!getPhoto_100().equals(object.getPhoto_100())) {
            differences.add(UserDifferences.DIFFERENT_PHOTO_100);
        }

        return differences;
    }

    public enum UserDifferences {
        DIFFERENT_ID,
        DIFFERENT_ONLINE,
        DIFFERENT_FIRST_NAME,
        DIFFERENT_LAST_NAME,
        DIFFERENT_PHOTO_100
    }
}
