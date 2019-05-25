package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Preview implements Parcelable {
    public static final Creator<Preview> CREATOR = new Creator<Preview>() {
        @Override
        public Preview createFromParcel(Parcel in) {
            return new Preview(in);
        }

        @Override
        public Preview[] newArray(int size) {
            return new Preview[size];
        }
    };
    @SerializedName("photo")
    private Photo photo;

    protected Preview(Parcel in) {
        photo = in.readParcelable(Photo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Photo getPhoto() {
        return photo;
    }
}
