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
    @SerializedName("photo_320")
    private String photo320Url;
    @SerializedName("photo_640")
    private String photo640Url;
    @SerializedName("photo_800")
    private String photo800Url;

    protected Video(Parcel in) {
        id = in.readInt();
        photo320Url = in.readString();
        photo640Url = in.readString();
        photo800Url = in.readString();
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

    public String getPhoto320Url() {
        return photo320Url;
    }

    public String getPhoto640Url() {
        return photo640Url;
    }

    public String getPhoto800Url() {
        return photo800Url;
    }
}
