package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class VkDate implements Parcelable {
    public static final Creator<VkDate> CREATOR = new Creator<VkDate>() {
        @Override
        public VkDate createFromParcel(Parcel in) {
            return new VkDate(in);
        }

        @Override
        public VkDate[] newArray(int size) {
            return new VkDate[size];
        }
    };
    private String dateString;

    public VkDate(String dateString) {
        this.dateString = dateString;
    }

    protected VkDate(Parcel in) {
        dateString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return dateString;
    }
}
