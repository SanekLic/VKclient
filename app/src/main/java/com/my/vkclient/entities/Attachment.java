package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Attachment implements Parcelable {
    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
    @SerializedName("type")
    private String type;
    @SerializedName("photo")
    private Photo photo;

    protected Attachment(Parcel in) {
        type = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(photo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public Photo getPhoto() {
        return photo;
    }

    @StringDef({Type.Photo, Type.Audio, Type.Video})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        String Photo = "photo";
        String Audio = "audio";
        String Video = "video";
    }
}
