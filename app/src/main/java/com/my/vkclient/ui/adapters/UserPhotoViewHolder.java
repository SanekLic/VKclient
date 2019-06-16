package com.my.vkclient.ui.adapters;

import android.support.annotation.AnimRes;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.SharedPreferencesHelper;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;

class UserPhotoViewHolder extends BaseViewHolder<Photo> {
    private ImageView userPhotoImageView;

    UserPhotoViewHolder(View itemView) {
        super(itemView);

        userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
    }

    public void bind(Photo photo) {
        ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, photo.getPhotoUrl(), photo.getPhotoWidth(), photo.getPhotoHeight(), getAnimation());
    }

    @AnimRes
    private int getAnimation() {
        if (SharedPreferencesHelper.getInstance().isAnimationEnable()) {
            return R.anim.item_scale_fade_in_anim;
        } else {
            return DEFAULT_ANIMATION;
        }
    }
}
