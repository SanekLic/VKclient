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
    private Boolean userLikes;
    @SerializedName("can_like")
    private Boolean canLike;
    @SerializedName("can_publish")
    private Boolean canPublish;

    protected Likes(Parcel in) {
        count = in.readInt();
        byte tmpUserLikes = in.readByte();
        userLikes = tmpUserLikes == 0 ? null : tmpUserLikes == 1;
        byte tmpCanLike = in.readByte();
        canLike = tmpCanLike == 0 ? null : tmpCanLike == 1;
        byte tmpCanPublish = in.readByte();
        canPublish = tmpCanPublish == 0 ? null : tmpCanPublish == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeByte((byte) (userLikes == null ? 0 : userLikes ? 1 : 2));
        dest.writeByte((byte) (canLike == null ? 0 : canLike ? 1 : 2));
        dest.writeByte((byte) (canPublish == null ? 0 : canPublish ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return count;
    }

    public Boolean getUserLikes() {
        return userLikes;
    }

    public Boolean getCanLike() {
        return canLike;
    }

    public Boolean getCanPublish() {
        return canPublish;
    }
}
