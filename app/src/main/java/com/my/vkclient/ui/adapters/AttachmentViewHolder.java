package com.my.vkclient.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.AttachmentPhoto;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;

class AttachmentViewHolder extends RecyclerView.ViewHolder {

    private TextView attachmentTitleTextView;
    private ImageView attachImageView;
    private ResultCallback<String> onAttachmentClickListener;
    private Attachment attachment;
    private String contentUrl;
    private TextView attachmentTypeTextView;

    public AttachmentViewHolder(View itemView) {
        super(itemView);

        setupView(itemView);
    }

    private void setupView(View itemView) {
        attachImageView = itemView.findViewById(R.id.attachImageView);
        attachImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAttachmentClickListener != null) {
                    onAttachmentClickListener.onResult(contentUrl);
                }
            }
        });
        attachmentTitleTextView = itemView.findViewById(R.id.attachmentTitleTextView);
        attachmentTypeTextView = itemView.findViewById(R.id.attachmentTypeTextView);
    }

    public void bind(Attachment attachment) {
        this.attachment = attachment;

        if (Attachment.Type.Photo.equals(this.attachment.getType())) {
            attachmentTitleTextView.setText("Photo");
            attachmentTypeTextView.setText("Фото");
            contentUrl = "";
            setAttachmentImage(attachment.getPhoto());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            attachmentTitleTextView.setText("Doc");
            attachmentTypeTextView.setText("Документ");
            contentUrl = "";
            setAttachmentImage(attachment.getDoc());
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            attachmentTitleTextView.setText(attachment.getVideo().getTitle());
            attachmentTypeTextView.setText("Видео");
            contentUrl = "";
            setAttachmentImage(attachment.getVideo());
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            attachmentTitleTextView.setText("Link");
            attachmentTypeTextView.setText("Ссылка");
            contentUrl = attachment.getLink().getUrl();
            setAttachmentImage(attachment.getLink());
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            attachmentTitleTextView.setText(attachment.getPodcast().getTitle());
            attachmentTypeTextView.setText("Подкаст");
            contentUrl = attachment.getPodcast().getUrl();
            setAttachmentImage(attachment.getPodcast());
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            attachmentTitleTextView.setText(attachment.getAudio().getTitle());
            attachmentTypeTextView.setText("Аудио");
            contentUrl = attachment.getAudio().getUrl();
            attachImageView.setImageResource(R.drawable.ic_music);
        } else {
            attachmentTitleTextView.setText("");
            attachmentTypeTextView.setText("");
            contentUrl = "";
            attachImageView.setImageDrawable(null);
        }
    }

    public void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        this.onAttachmentClickListener = onAttachmentClickListener;
    }

    private void setAttachmentImage(AttachmentPhoto attachmentPhoto) {
        if (attachmentPhoto != null && attachmentPhoto.getPhotoUrl() != null) {
            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachmentPhoto.getPhotoUrl(),
                    attachmentPhoto.getPhotoWidth(), attachmentPhoto.getPhotoHeight());
        } else{
            attachImageView.setImageDrawable(null);
        }
    }
}
