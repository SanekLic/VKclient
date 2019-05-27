package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Link implements Parcelable {
    public static final Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel in) {
            return new Link(in);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };
    @SerializedName("url")
    private String url;
    @SerializedName("photo")
    private Photo photo;

    protected Link(Parcel in) {
        url = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeParcelable(photo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return url;
    }

    public Photo getPhoto() {
        return photo;
    }
}
