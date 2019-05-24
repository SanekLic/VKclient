package com.my.vkclient.ui.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Size;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private ImageView attachImageView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        attachImageView = itemView.findViewById(R.id.attachImageView);
    }

    public void bind(Attachment attachment) {
        if (Attachment.Type.Photo.equals(attachment.getType())) {
            int maxWidth = Integer.MAX_VALUE;
            String urlPhoto = attachment.getPhoto().getSizes().get(0).getUrl();

            for (Size size : attachment.getPhoto().getSizes()) {
                if (Math.abs(size.getHeight() - 192) < maxWidth) {
                    maxWidth = Math.abs(size.getHeight() - 192);
                    urlPhoto = size.getUrl();
                }
            }

            ImageLoader.getImageFromUrl(attachImageView, urlPhoto);
        }
    }
}
