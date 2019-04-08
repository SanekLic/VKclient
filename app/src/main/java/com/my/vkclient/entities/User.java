package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("online")
    private int online;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("photo_max_orig")
    private String photoMaxOrig;

    @SerializedName("photo_100")
    private String photo100;

    @SerializedName("crop_photo")
    private CropPhoto cropPhoto;

    public int getId() {
        return id;
    }

    public int getOnline() {
        return online;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoMaxOrig() {
        return photoMaxOrig;
    }

    public String getPhoto100() {
        return photo100;
    }

    public CropPhoto getCropPhoto() {
        return cropPhoto;
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

        if (!getFirstName().equals(object.getFirstName())) {
            differences.add(UserDifferences.DIFFERENT_FIRST_NAME);
        }

        if (!getLastName().equals(object.getLastName())) {
            differences.add(UserDifferences.DIFFERENT_LAST_NAME);
        }

        if (!getPhoto100().equals(object.getPhoto100())) {
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
