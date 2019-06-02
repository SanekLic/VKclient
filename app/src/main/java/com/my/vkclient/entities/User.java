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

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        online = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        photoMaxUrl = in.readString();
        photo100Url = in.readString();
        cropPhoto = in.readParcelable(CropPhoto.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(online);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photoMaxUrl);
        dest.writeString(photo100Url);
        dest.writeParcelable(cropPhoto, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoMaxUrl() {
        return photoMaxUrl;
    }

    public void setPhotoMaxUrl(String photoMaxUrl) {
        this.photoMaxUrl = photoMaxUrl;
    }

    public String getPhoto100Url() {
        return photo100Url;
    }

    public void setPhoto100Url(String photo100Url) {
        this.photo100Url = photo100Url;
    }

    public CropPhoto getCropPhoto() {
        return cropPhoto;
    }

    public void setCropPhoto(CropPhoto cropPhoto) {
        this.cropPhoto = cropPhoto;
    }
}
