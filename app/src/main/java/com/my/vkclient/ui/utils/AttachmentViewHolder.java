package com.my.vkclient.ui.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private ImageView attachImageView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        attachImageView = itemView.findViewById(R.id.attachImageView);
    }

    public void bind(Attachment attachment) {
        if (attachment.getType().equals(Attachment.Type.Photo)) {
            ImageLoader.getImageFromUrl(attachImageView, attachment.getPhoto().getSizes().get(0).getUrl());
        }
    }
}
