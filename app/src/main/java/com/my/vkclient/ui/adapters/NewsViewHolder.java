package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.AttachmentPhoto;
import com.my.vkclient.entities.News;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

class NewsViewHolder extends BaseViewHolder<News> {
    private TextView likesTextView;
    private TextView commentsTextView;
    private TextView repostsTextView;
    private TextView viewsTextView;
    private ImageView sourceIconImageView;
    private TextView sourceNameTextView;
    private TextView sourceNewsDateTextView;
    private ImageView fromIconImageView;
    private TextView fromNameTextView;
    private TextView fromNewsDateTextView;
    private TextView newsTextView;
    private ImageView newsPhotoImageView;
    private RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;
    private News news;
    private ResultCallback<News> onLikeClickListener;

    public NewsViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        setupView(itemView);
        setupAttachmentRecyclerView();
    }

    private void setupView(View itemView) {
        sourceIconImageView = itemView.findViewById(R.id.sourceIconImageView);
        sourceNameTextView = itemView.findViewById(R.id.sourceNameTextView);
        sourceNewsDateTextView = itemView.findViewById(R.id.sourceNewsDateTextView);
        fromIconImageView = itemView.findViewById(R.id.fromIconImageView);
        fromNameTextView = itemView.findViewById(R.id.fromNameTextView);
        fromNewsDateTextView = itemView.findViewById(R.id.fromNewsDateTextView);
        newsTextView = itemView.findViewById(R.id.newsTextView);
        newsPhotoImageView = itemView.findViewById(R.id.newsPhotoImageView);
        attachmentRecyclerView = itemView.findViewById(R.id.attachmentRecyclerView);
        likesTextView = itemView.findViewById(R.id.likesTextView);
        likesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLikeClickListener != null && news != null) {
                    onLikeClickListener.onResult(news);
                }
            }
        });
        commentsTextView = itemView.findViewById(R.id.commentsTextView);
        repostsTextView = itemView.findViewById(R.id.repostsTextView);
        viewsTextView = itemView.findViewById(R.id.viewsTextView);
    }

    void setOnLikeClickListener(ResultCallback<News> onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    public void bind(News news) {
        this.news = news;

        sourceNewsDateTextView.setText(Utils.getInstance().getSimpleDate(news.getDate()));
        setNewsSourceInfo(news, sourceNameTextView, sourceIconImageView);

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(0);
            fromNewsDateTextView.setText(Utils.getInstance().getSimpleDate(newsCopy.getDate()));
            setNewsSourceInfo(newsCopy, fromNameTextView, fromIconImageView);
            setNewsText(newsCopy.getText());
            setAttachments(newsCopy.getAttachments());
            setVisibilityCopyNews(View.VISIBLE);
        } else {
            setNewsText(news.getText());
            setAttachments(news.getAttachments());
            setVisibilityCopyNews(View.GONE);
        }

        if (news.getLikes().getUserLikes()) {
            likesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_fill_likes), null, null, null);
        } else {
            likesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_likes), null, null, null);
        }

        likesTextView.setText(Utils.getInstance().formatNumber(news.getLikes().getCount()));
        commentsTextView.setText(Utils.getInstance().formatNumber(news.getComments().getCount()));
        repostsTextView.setText(Utils.getInstance().formatNumber(news.getReposts().getCount()));

        if (news.getViews() != null) {
            viewsTextView.setText(Utils.getInstance().formatNumber(news.getViews().getCount()));
        }
    }

    private void setNewsSourceInfo(News news, TextView nameTextView, ImageView iconImageView) {
        if (news.getGroup() != null) {
            nameTextView.setText(news.getGroup().getName());
            iconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
            ImageLoader.getInstance().getImageFromUrl(iconImageView, news.getGroup().getPhoto100Url(), 0, 0);
        } else if (news.getUser() != null) {
            nameTextView.setText(String.format(Constants.NAME_FORMAT, news.getUser().getFirstName(), news.getUser().getLastName()));
            iconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
            ImageLoader.getInstance().getImageFromUrl(iconImageView, news.getUser().getPhoto100Url(), 0, 0);
        }
    }

    private void setNewsText(String text) {
        newsTextView.setText(text);
        if (text == null || text.isEmpty()) {
            if (newsTextView.getVisibility() != View.GONE) {
                newsTextView.setVisibility(View.GONE);
            }
        } else {
            if (newsTextView.getVisibility() != View.VISIBLE) {
                newsTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAttachments(List<Attachment> newAttachments) {
        newsPhotoImageView.setImageDrawable(null);
        attachments.clear();

        if (newAttachments != null) {
            attachments.addAll(newAttachments);

            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);

                if (Attachment.Type.Photo.equals(attachment.getType())) {
                    setAttachmentImage(attachment.getPhoto());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Doc.equals(attachment.getType())) {
                    setAttachmentImage(attachment.getDoc());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Video.equals(attachment.getType())) {
                    setAttachmentImage(attachment.getVideo());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Link.equals(attachment.getType())) {
                    setAttachmentImage(attachment.getLink());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Podcast.equals(attachment.getType())) {
                    setAttachmentImage(attachment.getPodcast());
                    attachments.remove(i);

                    break;
                }
            }
        }

        attachmentRecyclerViewAdapter.setItems(attachments);
    }

    private void setAttachmentImage(AttachmentPhoto attachmentPhoto) {
        ImageLoader.getInstance().getImageFromUrl(newsPhotoImageView, attachmentPhoto.getPhotoUrl(), attachmentPhoto.getPhotoWidth(), attachmentPhoto.getPhotoHeight());
    }

    private void setVisibilityCopyNews(int visible) {
        if (fromIconImageView.getVisibility() != visible) {
            fromIconImageView.setVisibility(visible);
            fromNameTextView.setVisibility(visible);
            fromNewsDateTextView.setVisibility(visible);
        }
    }

    private void setupAttachmentRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(linearLayoutManager);
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
