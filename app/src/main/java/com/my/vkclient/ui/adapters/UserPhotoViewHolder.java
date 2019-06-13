package com.my.vkclient.ui.adapters;

import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.utils.ImageLoader;

class UserPhotoViewHolder extends BaseViewHolder<Photo> {
    private ImageView userPhotoImageView;

    UserPhotoViewHolder(View itemView) {
        super(itemView);

        userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
    }

    public void bind(Photo photo) {
        ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, photo.getPhotoUrl(), photo.getPhotoWidth(), photo.getPhotoHeight());
    }
}
