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
    @SerializedName("post_id")
    private int id;
    @SerializedName("type")
    private String type;
    @SerializedName("source_id")
    private int sourceId;
    @SerializedName("from_id")
    private int fromId;
    @SerializedName("date")
    private long date;
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

    private Group group;
    private User user;

    protected News(Parcel in) {
        id = in.readInt();
        type = in.readString();
        sourceId = in.readInt();
        fromId = in.readInt();
        date = in.readLong();
        text = in.readString();
        copyHistory = in.createTypedArrayList(News.CREATOR);
        comments = in.readParcelable(Comments.class.getClassLoader());
        likes = in.readParcelable(Likes.class.getClassLoader());
        reposts = in.readParcelable(Reposts.class.getClassLoader());
        views = in.readParcelable(Views.class.getClassLoader());
        attachments = in.createTypedArrayList(Attachment.CREATOR);
        group = in.readParcelable(Group.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    public News() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeInt(sourceId);
        dest.writeInt(fromId);
        dest.writeLong(date);
        dest.writeString(text);
        dest.writeTypedList(copyHistory);
        dest.writeParcelable(comments, flags);
        dest.writeParcelable(likes, flags);
        dest.writeParcelable(reposts, flags);
        dest.writeParcelable(views, flags);
        dest.writeTypedList(attachments);
        dest.writeParcelable(group, flags);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<News> getCopyHistory() {
        return copyHistory;
    }

    public void setCopyHistory(List<News> copyHistory) {
        this.copyHistory = copyHistory;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Reposts getReposts() {
        return reposts;
    }

    public void setReposts(Reposts reposts) {
        this.reposts = reposts;
    }

    public Views getViews() {
        return views;
    }

    public void setViews(Views views) {
        this.views = views;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
