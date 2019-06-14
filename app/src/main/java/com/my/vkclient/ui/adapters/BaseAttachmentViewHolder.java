package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.AttachmentPhoto;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;

import static com.my.vkclient.Constants.AUDIO_FORMAT;
import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIM;
import static com.my.vkclient.Constants.SEARCH_IN_GOOGLE_FORMAT;
import static com.my.vkclient.Constants.STRING_EMPTY;

abstract class BaseAttachmentViewHolder<T> extends BaseViewHolder<T> {
    ImageView attachImageView;
    private TextView attachmentInfoTextView;
    private TextView attachmentTypeTextView;
    private ResultCallback<String> onAttachmentClickListener;
    private ResultCallback<String> onPhotoClickListener;
    private Attachment attachment;
    private String contentUrl;
    private Context context;

    BaseAttachmentViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;

        setupView(itemView);
    }

    public abstract void bind(T data);

    void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        this.onAttachmentClickListener = onAttachmentClickListener;
    }

    void setOnPhotoClickListener(ResultCallback<String> onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    boolean bindAttachment(Attachment attachment) {
        this.attachment = attachment;

        if (Attachment.Type.Photo.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, STRING_EMPTY);
            setTextAndVisibilityTextView(attachmentTypeTextView, context.getResources().getString(R.string.photo_attachment_type_label));
            contentUrl = STRING_EMPTY;
            setAttachmentImage(attachment.getPhoto());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, attachment.getDoc().getTitle());
            setTextAndVisibilityTextView(attachmentTypeTextView, attachment.getDoc().getExt());
            contentUrl = attachment.getDoc().getUrl();

            if (attachment.getDoc().getPhotoUrl() == null) {
                setAttachmentImage(R.drawable.ic_attachment_doc);
            } else {
                setAttachmentImage(attachment.getDoc());
            }
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, attachment.getVideo().getTitle());
            setTextAndVisibilityTextView(attachmentTypeTextView, context.getResources().getString(R.string.video_attachment_type_label));
            contentUrl = String.format(SEARCH_IN_GOOGLE_FORMAT, attachment.getVideo().getTitle(), STRING_EMPTY);
            setAttachmentImage(attachment.getVideo());
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, attachment.getLink().getTitle());
            setTextAndVisibilityTextView(attachmentTypeTextView, context.getResources().getString(R.string.link_attachment_type_label));
            contentUrl = attachment.getLink().getUrl();

            if (attachment.getLink().getPhotoUrl() == null) {
                setAttachmentImage(R.drawable.ic_attachment_link);
            } else {
                setAttachmentImage(attachment.getLink());
            }
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, attachment.getPodcast().getTitle());
            setTextAndVisibilityTextView(attachmentTypeTextView, context.getResources().getString(R.string.podcast_attachment_type_label));
            contentUrl = attachment.getPodcast().getUrl();
            setAttachmentImage(attachment.getPodcast());
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            setTextAndVisibilityTextView(attachmentInfoTextView, String.format(AUDIO_FORMAT, attachment.getAudio().getTitle(), attachment.getAudio().getArtist()));
            setTextAndVisibilityTextView(attachmentTypeTextView, context.getResources().getString(R.string.audio_attachment_type_label));
            contentUrl = String.format(SEARCH_IN_GOOGLE_FORMAT, attachment.getAudio().getArtist(), attachment.getAudio().getTitle());
            setAttachmentImage(R.drawable.ic_attachment_audio);
        } else {
            setTextAndVisibilityTextView(attachmentInfoTextView, STRING_EMPTY);
            setTextAndVisibilityTextView(attachmentTypeTextView, STRING_EMPTY);
            contentUrl = STRING_EMPTY;
            setAttachmentImage(null);

            return false;
        }

        return true;
    }

    void setTextAndVisibilityTextView(TextView textView, String text) {
        if (text == null || text.isEmpty()) {
            if (textView.getVisibility() != View.GONE) {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setText(text);

            if (textView.getVisibility() != View.VISIBLE) {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupView(View itemView) {
        attachImageView = itemView.findViewById(R.id.attachmentImageView);
        attachImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Attachment.Type.Photo.equals(attachment.getType())) {
                    if (onPhotoClickListener != null) {
                        onPhotoClickListener.onResult(attachment.getPhoto().getPhotoUrl());
                    }
                } else if (onAttachmentClickListener != null) {
                    onAttachmentClickListener.onResult(contentUrl);
                }
            }
        });
        attachmentInfoTextView = itemView.findViewById(R.id.attachmentInfoTextView);
        attachmentTypeTextView = itemView.findViewById(R.id.attachmentTypeTextView);
    }

    private void setAttachmentImage(AttachmentPhoto attachmentPhoto) {
        if (attachmentPhoto != null) {
            if (attachImageView.getVisibility() != View.VISIBLE) {
                attachImageView.setVisibility(View.VISIBLE);
            }

            ImageLoader.getInstance().getImageFromUrl(attachImageView, attachmentPhoto.getPhotoUrl(),
                    attachmentPhoto.getPhotoWidth(), attachmentPhoto.getPhotoHeight(), DEFAULT_ANIM);
        } else {
            attachImageView.setImageDrawable(null);

            if (attachImageView.getVisibility() != View.GONE) {
                attachImageView.setVisibility(View.GONE);
            }
        }
    }

    private void setAttachmentImage(@DrawableRes int resId) {
        attachImageView.setTag(R.id.IMAGE_TAG_URL, null);
        attachImageView.setImageResource(resId);
    }
}
