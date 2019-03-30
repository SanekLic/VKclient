package com.my.vkclient;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class FriendViewHolder extends RecyclerView.ViewHolder {
    private static final String FRIEND_STATUS_OFFLINE_STRING = "(offline)";
    private static final String FRIEND_STATUS_ONLINE_STRING = "(online)";
    private ImageView friendPhotoView;
    private TextView friendNameView;
    private TextView onlineStatusTextView;
    private ImageView onlineStatusImageView;
    private LoadImageToImageViewAsync loadImageToImageViewAsync;

    public FriendViewHolder(View itemView) {
        super(itemView);
        this.friendPhotoView = itemView.findViewById(R.id.friendPhotoView);
        this.friendNameView = itemView.findViewById(R.id.friendNameView);
        this.onlineStatusTextView = itemView.findViewById(R.id.onlineStatusTextView);
        this.onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
    }

    public void bind(Friend friend, ArrayList<Friend.FriendDifferences> differences) {
        if (differences == null
                || differences.contains(Friend.FriendDifferences.DIFFERENT_FIRST_NAME)
                || differences.contains(Friend.FriendDifferences.DIFFERENT_LAST_NAME)) {
            this.friendNameView.setText(new StringBuilder()
                    .append(friend.getFirst_name())
                    .append(" ")
                    .append(friend.getLast_name()).toString());
        }

        if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_PHOTO_100)) {
            this.friendPhotoView.setImageDrawable(null);
            this.friendPhotoView.setAlpha(0f);
            this.loadImageToImageViewAsync = new LoadImageToImageViewAsync(this.friendPhotoView, true);
            this.loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, friend.getPhoto_100());
        }

        if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_ONLINE)) {
            if (friend.getOnline() == 0) {
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                this.onlineStatusTextView.setText(FRIEND_STATUS_OFFLINE_STRING);
            } else {
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                this.onlineStatusTextView.setText(FRIEND_STATUS_ONLINE_STRING);
            }
        }
    }

    public void recycled() {
        this.loadImageToImageViewAsync.cancel(false);
    }
}
