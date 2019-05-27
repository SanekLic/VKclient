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
    private String previewPhotoUrl;
    @SerializedName("photo_1280")
    private String maxPhotoUrl;

    protected Video(Parcel in) {
        id = in.readInt();
        previewPhotoUrl = in.readString();
        maxPhotoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(previewPhotoUrl);
        dest.writeString(maxPhotoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getPreviewPhotoUrl() {
        return previewPhotoUrl;
    }

    public String getMaxPhotoUrl() {
        return maxPhotoUrl;
    }

}
