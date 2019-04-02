package com.my.vkclient;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Entities.Friend;

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
        friendPhotoView = itemView.findViewById(R.id.friendPhotoImageView);
        friendNameView = itemView.findViewById(R.id.friendNameTextView);
        onlineStatusTextView = itemView.findViewById(R.id.onlineStatusTextView);
        onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
    }

    public void bind(Friend friend, ArrayList<Friend.FriendDifferences> differences) {
        if (differences == null
                || differences.contains(Friend.FriendDifferences.DIFFERENT_FIRST_NAME)
                || differences.contains(Friend.FriendDifferences.DIFFERENT_LAST_NAME)) {
            friendNameView.setText(new StringBuilder()
                    .append(friend.getFirst_name())
                    .append(" ")
                    .append(friend.getLast_name()).toString());
        }

        if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_PHOTO_100)) {
            friendPhotoView.setImageDrawable(null);
            friendPhotoView.setAlpha(0f);
            loadImageToImageViewAsync = new LoadImageToImageViewAsync(friendPhotoView).setCircular(true);
            loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, friend.getPhoto_100());
        }

        if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_ONLINE)) {
            if (friend.getOnline() == 0) {
                onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                onlineStatusTextView.setText(FRIEND_STATUS_OFFLINE_STRING);
            } else {
                onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                onlineStatusTextView.setText(FRIEND_STATUS_ONLINE_STRING);
            }
        }
    }

    public void recycled() {
        loadImageToImageViewAsync.cancel(false);
    }
}
