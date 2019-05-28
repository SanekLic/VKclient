package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("online")
    private int online;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("photo_max_orig")
    private String photoMaxUrl;
    @SerializedName("photo_100")
    private String photo100Url;
    @SerializedName("crop_photo")
    private CropPhoto cropPhoto;

    protected User(Parcel in) {
        id = in.readInt();
        online = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        photoMaxUrl = in.readString();
        photo100Url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(online);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photoMaxUrl);
        dest.writeString(photo100Url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getPhotoMaxUrl() {
        return photoMaxUrl;
    }

    public String getPhoto100Url() {
        return photo100Url;
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

        if (!getPhoto100Url().equals(object.getPhoto100Url())) {
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
