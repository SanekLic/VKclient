package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PodcastInfo implements Parcelable {
    public static final Creator<PodcastInfo> CREATOR = new Creator<PodcastInfo>() {
        @Override
        public PodcastInfo createFromParcel(Parcel in) {
            return new PodcastInfo(in);
        }

        @Override
        public PodcastInfo[] newArray(int size) {
            return new PodcastInfo[size];
        }
    };
    @SerializedName("cover")
    private Photo cover;

    protected PodcastInfo(Parcel in) {
        cover = in.readParcelable(Photo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cover, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Photo getCover() {
        return cover;
    }
}
