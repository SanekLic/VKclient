package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Attachment implements Parcelable {
    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
    @SerializedName("type")
    private String type;
    @SerializedName("photo")
    private Photo photo;
    @SerializedName("video")
    private Video video;
    @SerializedName("doc")
    private Doc doc;
    @SerializedName("link")
    private Link link;
    @SerializedName("podcast")
    private Podcast podcast;

    protected Attachment(Parcel in) {
        type = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
        video = in.readParcelable(Video.class.getClassLoader());
        doc = in.readParcelable(Doc.class.getClassLoader());
        link = in.readParcelable(Link.class.getClassLoader());
        podcast = in.readParcelable(Podcast.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(photo, flags);
        dest.writeParcelable(video, flags);
        dest.writeParcelable(doc, flags);
        dest.writeParcelable(link, flags);
        dest.writeParcelable(podcast, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Video getVideo() {
        return video;
    }

    public Doc getDoc() {
        return doc;
    }

    public Link getLink() {
        return link;
    }

    public Podcast getPodcast() {
        return podcast;
    }

    @StringDef({Type.Photo, Type.Doc, Type.Audio, Type.Video, Type.Link, Type.Podcast})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        String Photo = "photo";
        String Doc = "doc";
        String Audio = "audio";
        String Video = "video";
        String Link = "link";
        String Podcast = "podcast";
    }
}
