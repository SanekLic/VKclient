package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.News;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

class NewsViewHolder extends BaseAttachmentViewHolder<News> {
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
    private android.support.v7.widget.RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;
    private News news;
    private ResultCallback<News> onLikeClickListener;
    private ResultCallback<String> onNewsAttachmentClickListener;
    private ResultCallback<String> onNewsPhotoClickListener;

    NewsViewHolder(Context context, View itemView) {
        super(context, itemView);

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

    void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        super.setOnAttachmentClickListener(onAttachmentClickListener);

        this.onNewsAttachmentClickListener = onAttachmentClickListener;

    }

    void setOnPhotoClickListener(ResultCallback<String> onPhotoClickListener) {
        super.setOnPhotoClickListener(onPhotoClickListener);

        this.onNewsPhotoClickListener = onPhotoClickListener;
    }

    public void bind(News news) {
        this.news = news;

        sourceNewsDateTextView.setText(Utils.getInstance().getSimpleDate(news.getDate()));
        setNewsSourceInfo(news, sourceNameTextView, sourceIconImageView);

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(0);
            fromNewsDateTextView.setText(Utils.getInstance().getSimpleDate(newsCopy.getDate()));
            setNewsSourceInfo(newsCopy, fromNameTextView, fromIconImageView);
            setTextAndVisibilityTextView(newsTextView, newsCopy.getText());
            setAttachments(newsCopy.getAttachments());
            setVisibilityCopyNews(View.VISIBLE);
        } else {
            setTextAndVisibilityTextView(newsTextView, news.getText());
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

    private void setAttachments(List<Attachment> newAttachments) {
        attachImageView.setImageDrawable(null);
        attachments.clear();

        if (newAttachments != null) {
            attachments.addAll(newAttachments);

            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);

                if (bindAttachment(attachment)) {
                    attachments.remove(i);

                    break;
                }
            }
        }

        attachmentRecyclerViewAdapter.setItems(attachments);
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
        attachmentRecyclerView.addItemDecoration(new android.support.v7.widget.RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull android.support.v7.widget.RecyclerView parent, @NonNull android.support.v7.widget.RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = Constants.RecyclerView.VIEW_MARGIN;
                }
            }
        });
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerViewAdapter.setOnAttachmentClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (onNewsAttachmentClickListener != null) {
                    onNewsAttachmentClickListener.onResult(result);
                }
            }
        });
        attachmentRecyclerViewAdapter.setOnPhotoClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (onNewsPhotoClickListener != null) {
                    onNewsPhotoClickListener.onResult(result);
                }
            }
        });
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
