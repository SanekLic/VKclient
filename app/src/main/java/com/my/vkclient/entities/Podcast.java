package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Podcast implements Parcelable {
    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("podcast_info")
    private PodcastInfo podcastInfo;

    protected Podcast(Parcel in) {
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        podcastInfo = in.readParcelable(PodcastInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeParcelable(podcastInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public PodcastInfo getPodcastInfo() {
        return podcastInfo;
    }
}
