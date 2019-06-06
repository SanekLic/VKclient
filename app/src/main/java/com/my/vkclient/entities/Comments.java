package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Comments implements Parcelable {
    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };
    @SerializedName("count")
    private int count;
    @SerializedName("can_post")
    private Boolean canPost;

    public Comments() {
    }

    protected Comments(Parcel in) {
        count = in.readInt();
        byte tmpCanPost = in.readByte();
        canPost = tmpCanPost == 0 ? null : tmpCanPost == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeByte((byte) (canPost == null ? 0 : canPost ? 1 : 2));
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

    public Boolean getCanPost() {
        return canPost;
    }

    public void setCanPost(Boolean canPost) {
        this.canPost = canPost;
    }
}
