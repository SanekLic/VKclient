package com.my.vkclient.ui.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.entities.User;

import java.util.ArrayList;

class FriendViewHolder extends RecyclerView.ViewHolder {
    private ImageView friendPhotoView;
    private TextView friendNameView;
    private ImageView onlineStatusImageView;

    public FriendViewHolder(View itemView) {
        super(itemView);

        friendPhotoView = itemView.findViewById(R.id.friendPhotoImageView);
        friendNameView = itemView.findViewById(R.id.friendNameTextView);
        onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
    }

    public void bind(User user, ArrayList<User.UserDifferences> differences) {
        if (differences == null
                || differences.contains(User.UserDifferences.DIFFERENT_FIRST_NAME)
                || differences.contains(User.UserDifferences.DIFFERENT_LAST_NAME)) {
            friendNameView.setText(new StringBuilder()
                    .append(user.getFirstName())
                    .append(" ")
                    .append(user.getLastName()).toString());
        }

        if (differences == null || differences.contains(User.UserDifferences.DIFFERENT_PHOTO_100)) {
            friendPhotoView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
            ImageLoader.getImageFromUrl(friendPhotoView, user.getPhoto100Url(), 0, 0);
        }

        if (differences == null || differences.contains(User.UserDifferences.DIFFERENT_ONLINE)) {
            if (user.getOnline() == 0) {
                if (onlineStatusImageView.getVisibility() != View.GONE) {
                    onlineStatusImageView.setVisibility(View.GONE);
                }
            } else {
                if (onlineStatusImageView.getVisibility() != View.VISIBLE) {
                    onlineStatusImageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
