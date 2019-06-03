package com.my.vkclient.ui.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ImageLoader;

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

    public void bind(User user) {
        friendNameView.setText(new StringBuilder()
                .append(user.getFirstName())
                .append(Constants.STRING_SPACE)
                .append(user.getLastName()).toString());

        friendPhotoView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
        ImageLoader.getImageFromUrl(friendPhotoView, user.getPhoto100Url(), 0, 0);

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
