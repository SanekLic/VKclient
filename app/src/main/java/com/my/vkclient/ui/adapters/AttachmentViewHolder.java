package com.my.vkclient.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.AttachmentPhoto;
import com.my.vkclient.utils.ImageLoader;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private ImageView attachImageView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        attachImageView = itemView.findViewById(R.id.attachImageView);
    }

    public void bind(Attachment attachment) {
        if (Attachment.Type.Photo.equals(attachment.getType())) {
            setAttachmentImage(attachment.getPhoto());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            setAttachmentImage(attachment.getDoc());
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            setAttachmentImage(attachment.getVideo());
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            setAttachmentImage(attachment.getLink());
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            setAttachmentImage(attachment.getPodcast());
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            attachImageView.setImageResource(R.drawable.ic_music);
        } else {
            attachImageView.setImageDrawable(null);
        }
    }

    private void setAttachmentImage(AttachmentPhoto attachmentPhoto) {
        if (attachmentPhoto != null) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachmentPhoto.getPhotoUrl(), attachmentPhoto.getPhotoWidth(), attachmentPhoto.getPhotoHeight());
        }
    }
}
