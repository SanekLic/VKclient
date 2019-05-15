package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CropPhoto implements Parcelable {
    public static final Creator<CropPhoto> CREATOR = new Creator<CropPhoto>() {
        @Override
        public CropPhoto createFromParcel(Parcel in) {
            return new CropPhoto(in);
        }

        @Override
        public CropPhoto[] newArray(int size) {
            return new CropPhoto[size];
        }
    };

    @SerializedName("photo")
    private Photo photo;

    @SerializedName("rect")
    private Rect rect;

    @SerializedName("crop")
    private Rect crop;

    protected CropPhoto(Parcel in) {
        photo = in.readParcelable(Photo.class.getClassLoader());
        rect = in.readParcelable(Rect.class.getClassLoader());
        crop = in.readParcelable(Rect.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photo, flags);
        dest.writeParcelable(rect, flags);
        dest.writeParcelable(crop, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Rect getRect() {
        return rect;
    }

    public Rect getCrop() {
        return crop;
    }
}
