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
    private Boolean userReposted;

    public Reposts() {
    }

    protected Reposts(Parcel in) {
        count = in.readInt();
        byte tmpUserReposted = in.readByte();
        userReposted = tmpUserReposted == 0 ? null : tmpUserReposted == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeByte((byte) (userReposted == null ? 0 : userReposted ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getUserReposted() {
        return userReposted;
    }

    public void setUserReposted(Boolean userReposted) {
        this.userReposted = userReposted;
    }
}
