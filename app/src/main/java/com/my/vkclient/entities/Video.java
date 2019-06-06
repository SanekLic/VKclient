package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {
    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("photo_320")
    private String photo320Url;
    @SerializedName("photo_640")
    private String photo640Url;
    @SerializedName("photo_800")
    private String photo800Url;

    private String photoUrl;
    private int photoWidth;
    private int photoHeight;

    public Video() {
    }

    protected Video(Parcel in) {
        id = in.readInt();
        photo320Url = in.readString();
        photo640Url = in.readString();
        photo800Url = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getPhotoWidth() {
        return photoWidth;
    }

    public void setPhotoWidth(int photoWidth) {
        this.photoWidth = photoWidth;
    }

    public int getPhotoHeight() {
        return photoHeight;
    }

    public void setPhotoHeight(int photoHeight) {
        this.photoHeight = photoHeight;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(photo320Url);
        dest.writeString(photo640Url);
        dest.writeString(photo800Url);
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

    public String getPhoto320Url() {
        return photo320Url;
    }

    public void setPhoto320Url(String photo320Url) {
        this.photo320Url = photo320Url;
    }

    public String getPhoto640Url() {
        return photo640Url;
    }

    public void setPhoto640Url(String photo640Url) {
        this.photo640Url = photo640Url;
    }

    public String getPhoto800Url() {
        return photo800Url;
    }

    public void setPhoto800Url(String photo800Url) {
        this.photo800Url = photo800Url;
    }
}
