package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Reposts implements Parcelable {
    public static final Creator<Reposts> CREATOR = new Creator<Reposts>() {
        @Override
        public Reposts createFromParcel(Parcel in) {
            return new Reposts(in);
        }

        @Override
        public Reposts[] newArray(int size) {
            return new Reposts[size];
        }
    };

    @SerializedName("count")
    private int count;

    @SerializedName("user_reposted")
    private int userReposted;

    protected Reposts(Parcel in) {
        count = in.readInt();
        userReposted = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(userReposted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return count;
    }

    public int getUserReposted() {
        return userReposted;
    }

}
