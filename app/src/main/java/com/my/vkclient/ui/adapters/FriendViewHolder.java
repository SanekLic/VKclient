package com.my.vkclient.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ImageLoader;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;
import static com.my.vkclient.Constants.NAME_FORMAT;

class FriendViewHolder extends BaseViewHolder<User> {
    private ImageView friendPhotoView;
    private TextView friendNameView;
    private ImageView onlineStatusImageView;

    FriendViewHolder(View itemView) {
        super(itemView);

        friendPhotoView = itemView.findViewById(R.id.friendPhotoImageView);
        friendNameView = itemView.findViewById(R.id.friendNameTextView);
        onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
    }

    public void bind(User user) {
        friendNameView.setText(String.format(NAME_FORMAT, user.getFirstName(), user.getLastName()));

        friendPhotoView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
        ImageLoader.getInstance().getImageFromUrl(friendPhotoView, user.getPhoto100Url(), 0, 0, DEFAULT_ANIMATION);

        if (user.getOnline()) {
            if (onlineStatusImageView.getVisibility() != View.VISIBLE) {
                onlineStatusImageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (onlineStatusImageView.getVisibility() != View.GONE) {
                onlineStatusImageView.setVisibility(View.GONE);
            }
        }
    }
}
