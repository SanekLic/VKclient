package com.my.vkclient.ui.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Size;

import java.util.List;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private ImageView attachImageView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        attachImageView = itemView.findViewById(R.id.attachImageView);
    }

    public void bind(Attachment attachment) {
        if (Attachment.Type.Photo.equals(attachment.getType())) {
            setPhotoToImageView(attachment.getPhoto().getSizes());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            setPhotoToImageView(attachment.getDoc().getPreview().getPhoto().getSizes());
        } else if (Attachment.Type.Video.equals((attachment.getType()))) {
            ImageLoader.getImageFromUrl(attachImageView, attachment.getVideo().getPhoto320Url(), 320, 240);
        } else if (Attachment.Type.Link.equals(attachment.getType()) && attachment.getLink().getPhoto() != null) {
            setPhotoToImageView(attachment.getLink().getPhoto().getSizes());
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            setPhotoToImageView(attachment.getPodcast().getPodcastInfo().getCover().getSizes());
        } else {
            attachImageView.setImageDrawable(null);
        }
    }

    private void setPhotoToImageView(List<Size> photoSizes) {
        Size showSize = photoSizes.get(0);

        for (Size size : photoSizes) {
            if (Math.abs(size.getHeight() - 192) < Math.abs(showSize.getHeight() - 192)) {
                showSize = size;
            }
        }

        ImageLoader.getImageFromUrl(attachImageView, showSize.getUrl(), showSize.getWidth(), showSize.getHeight());
    }
}
