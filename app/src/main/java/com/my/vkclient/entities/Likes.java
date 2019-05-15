package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Likes implements Parcelable {
    public static final Creator<Likes> CREATOR = new Creator<Likes>() {
        @Override
        public Likes createFromParcel(Parcel in) {
            return new Likes(in);
        }

        @Override
        public Likes[] newArray(int size) {
            return new Likes[size];
        }
    };

    @SerializedName("count")
    private int count;

    @SerializedName("user_likes")
    private int userLikes;

    @SerializedName("can_like")
    private int canLike;

    @SerializedName("can_publish")
    private int canPublish;

    protected Likes(Parcel in) {
        count = in.readInt();
        userLikes = in.readInt();
        canLike = in.readInt();
        canPublish = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(userLikes);
        dest.writeInt(canLike);
        dest.writeInt(canPublish);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return count;
    }

    public int getUserLikes() {
        return userLikes;
    }

    public int getCanLike() {
        return canLike;
    }

    public int getCanPublish() {
        return canPublish;
    }
}
