package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Doc implements Parcelable {
    public static final Creator<Doc> CREATOR = new Creator<Doc>() {
        @Override
        public Doc createFromParcel(Parcel in) {
            return new Doc(in);
        }

        @Override
        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("url")
    private String url;
    @SerializedName("preview")
    private Preview preview;

    protected Doc(Parcel in) {
        id = in.readInt();
        url = in.readString();
        preview = in.readParcelable(Preview.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeParcelable(preview, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Preview getPreview() {
        return preview;
    }
}