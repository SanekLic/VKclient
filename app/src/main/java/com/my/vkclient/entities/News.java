package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class News implements Parcelable {
    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @SerializedName("type")
    private String type;

    @SerializedName("source_id")
    private int sourceId;

    @SerializedName("date")
    private long date;

    @SerializedName("text")
    private String text;

    @SerializedName("likes")
    private Likes likes;

    @SerializedName("reposts")
    private Reposts reposts;

    @SerializedName("views")
    private Views views;

    protected News(Parcel in) {
        type = in.readString();
        sourceId = in.readInt();
        date = in.readLong();
        text = in.readString();
        likes = in.readParcelable(Likes.class.getClassLoader());
        reposts = in.readParcelable(Reposts.class.getClassLoader());
        views = in.readParcelable(Views.class.getClassLoader());
    }

    public static Creator<News> getCREATOR() {
        return CREATOR;
    }

    public String getType() {
        return type;
    }

    public int getSourceId() {
        return sourceId;
    }

    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Likes getLikes() {
        return likes;
    }

    public Reposts getReposts() {
        return reposts;
    }

    public Views getViews() {
        return views;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(sourceId);
        dest.writeLong(date);
        dest.writeString(text);
        dest.writeParcelable(likes, flags);
        dest.writeParcelable(reposts, flags);
        dest.writeParcelable(views, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
