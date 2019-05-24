package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("from_id")
    private int fromId;
    @SerializedName("date")
    private VkDate date;
    @SerializedName("text")
    private String text;
    @SerializedName("copy_history")
    private List<News> copyHistory;
    @SerializedName("comments")
    private Comments comments;
    @SerializedName("likes")
    private Likes likes;
    @SerializedName("reposts")
    private Reposts reposts;
    @SerializedName("views")
    private Views views;
    @SerializedName("attachments")
    private List<Attachment> attachments;

    protected News(Parcel in) {
        type = in.readString();
        sourceId = in.readInt();
        fromId = in.readInt();
        date = in.readParcelable(VkDate.class.getClassLoader());
        text = in.readString();
        copyHistory = in.createTypedArrayList(News.CREATOR);
        comments = in.readParcelable(Comments.class.getClassLoader());
        likes = in.readParcelable(Likes.class.getClassLoader());
        reposts = in.readParcelable(Reposts.class.getClassLoader());
        views = in.readParcelable(Views.class.getClassLoader());
        attachments = in.createTypedArrayList(Attachment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(sourceId);
        dest.writeInt(fromId);
        dest.writeParcelable(date, flags);
        dest.writeString(text);
        dest.writeTypedList(copyHistory);
        dest.writeParcelable(comments, flags);
        dest.writeParcelable(likes, flags);
        dest.writeParcelable(reposts, flags);
        dest.writeParcelable(views, flags);
        dest.writeTypedList(attachments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getFromId() {
        return fromId;
    }

    public VkDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public List<News> getCopyHistory() {
        return copyHistory;
    }

    public Comments getComments() {
        return comments;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
