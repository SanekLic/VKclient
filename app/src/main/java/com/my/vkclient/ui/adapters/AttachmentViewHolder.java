package com.my.vkclient.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.utils.ImageLoader;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private ImageView attachImageView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        attachImageView = itemView.findViewById(R.id.attachImageView);
    }

    public void bind(Attachment attachment) {
        if (Attachment.Type.Photo.equals(attachment.getType())) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachment.getPhoto().getPhotoUrl(), attachment.getPhoto().getPhotoWidth(), attachment.getPhoto().getPhotoHeight());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachment.getDoc().getPhotoUrl(), attachment.getDoc().getPhotoWidth(), attachment.getDoc().getPhotoHeight());
        } else if (Attachment.Type.Video.equals((attachment.getType()))) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachment.getVideo().getPhotoUrl(), attachment.getVideo().getPhotoWidth(), attachment.getVideo().getPhotoHeight());
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachment.getLink().getPhotoUrl(), attachment.getLink().getPhotoWidth(), attachment.getLink().getPhotoHeight());
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachment.getPodcast().getPhotoUrl(), attachment.getPodcast().getPhotoWidth(), attachment.getPodcast().getPhotoHeight());
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            attachImageView.setImageResource(R.drawable.ic_music);
        } else {
            attachImageView.setImageDrawable(null);
        }
    }
}
