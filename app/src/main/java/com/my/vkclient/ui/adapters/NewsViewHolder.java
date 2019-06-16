package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;

class NewsViewHolder extends BaseAttachmentViewHolder<News> {
    private TextView likesTextView;
    private ImageView likesImageView;
    private TextView commentsTextView;
    private TextView repostsTextView;
    private TextView viewsTextView;
    private ImageView sourceIconImageView;
    private TextView sourceNameTextView;
    private TextView sourceNewsDateTextView;
    private ImageView fromIconImageView;
    private TextView fromNameTextView;
    private ImageView fromNameImageView;
    private TextView fromNewsDateTextView;
    private TextView newsTextView;
    private ImageView sourceVerifiedIconImageView;
    private ImageView fromVerifiedIconImageView;
    private android.support.v7.widget.RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;
    private News news;
    private ResultCallback<News> onLikeClickListener;
    private ResultCallback<String> onNewsAttachmentClickListener;
    private ResultCallback<String> onNewsPhotoClickListener;
    private ResultCallback<User> onUserClickListener;
    private ResultCallback<Group> onGroupClickListener;

    NewsViewHolder(Context context, View itemView) {
        super(context, itemView);

        this.context = context;
        setupView(itemView);
        setupAttachmentRecyclerView();
    }

    private void setupView(View itemView) {
        sourceIconImageView = itemView.findViewById(R.id.sourceIconImageView);
        sourceIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOwnerClick(news);
            }
        });
        sourceNameTextView = itemView.findViewById(R.id.sourceNameTextView);
        sourceNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOwnerClick(news);
            }
        });
        sourceNewsDateTextView = itemView.findViewById(R.id.sourceNewsDateTextView);
        fromIconImageView = itemView.findViewById(R.id.fromIconImageView);
        fromIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOwnerClick(news.getCopyHistory().get(0));
            }
        });
        fromNameTextView = itemView.findViewById(R.id.fromNameTextView);
        fromNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOwnerClick(news.getCopyHistory().get(0));
            }
        });
        fromNameImageView = itemView.findViewById(R.id.fromNameImageView);
        fromNewsDateTextView = itemView.findViewById(R.id.fromNewsDateTextView);
        newsTextView = itemView.findViewById(R.id.newsTextView);
        attachmentRecyclerView = itemView.findViewById(R.id.attachmentRecyclerView);
        likesTextView = itemView.findViewById(R.id.likesTextView);
        likesImageView = itemView.findViewById(R.id.likesImageView);
        likesImageView.setOnClickListener(new View.OnClickListener() {
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
        sourceVerifiedIconImageView = itemView.findViewById(R.id.sourceVerifiedIconImageView);
        fromVerifiedIconImageView = itemView.findViewById(R.id.fromVerifiedIconImageView);
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

    void setOnUserClickListener(ResultCallback<User> onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    void setOnGroupClickListener(ResultCallback<Group> onGroupClickListener) {
        this.onGroupClickListener = onGroupClickListener;
    }

    public void bind(News news) {
        this.news = news;

        sourceNewsDateTextView.setText(Utils.getInstance().getSimpleDate(news.getDate()));
        setNewsSourceInfo(news, sourceNameTextView, sourceIconImageView, sourceVerifiedIconImageView);

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(0);
            fromNewsDateTextView.setText(Utils.getInstance().getSimpleDate(newsCopy.getDate()));
            setNewsSourceInfo(newsCopy, fromNameTextView, fromIconImageView, fromVerifiedIconImageView);
            setTextAndVisibilityTextView(newsTextView, newsCopy.getText());
            setAttachments(newsCopy.getAttachments());
            setVisibilityCopyNews(View.VISIBLE);
        } else {
            setTextAndVisibilityTextView(newsTextView, news.getText());
            setAttachments(news.getAttachments());
            setVisibilityCopyNews(View.GONE);
        }

        if (news.getLikes().getUserLikes()) {
            likesImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorActionCheckIcon));
        } else {
            likesImageView.clearColorFilter();
        }

        likesTextView.setText(Utils.getInstance().formatNumber(news.getLikes().getCount()));
        commentsTextView.setText(Utils.getInstance().formatNumber(news.getComments().getCount()));
        repostsTextView.setText(Utils.getInstance().formatNumber(news.getReposts().getCount()));

        if (news.getViews() != null) {
            viewsTextView.setText(Utils.getInstance().formatNumber(news.getViews().getCount()));
        }
    }

    private void setNewsSourceInfo(News news, TextView nameTextView, ImageView iconImageView, ImageView verifiedImageView) {
        boolean verified = false;

        if (news.getGroup() != null) {
            verified = news.getGroup().getVerified();
            nameTextView.setText(news.getGroup().getName());
            iconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
            ImageLoader.getInstance().getImageFromUrl(iconImageView, news.getGroup().getPhoto100Url(), 0, 0, DEFAULT_ANIMATION);
        } else if (news.getUser() != null) {
            verified = news.getUser().getVerified();
            nameTextView.setText(String.format(Constants.NAME_FORMAT, news.getUser().getFirstName(), news.getUser().getLastName()));
            iconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
            ImageLoader.getInstance().getImageFromUrl(iconImageView, news.getUser().getPhoto100Url(), 0, 0, DEFAULT_ANIMATION);
        }

        if (verified) {
            if (verifiedImageView.getVisibility() != View.VISIBLE) {
                verifiedImageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (verifiedImageView.getVisibility() != View.GONE) {
                verifiedImageView.setVisibility(View.GONE);
            }
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
            fromNameImageView.setVisibility(visible);
        }

        if (visible == View.GONE && fromVerifiedIconImageView.getVisibility() != visible) {
            fromVerifiedIconImageView.setVisibility(visible);
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

    private void onOwnerClick(News news) {
        if (news != null) {
            if (news.getGroup() != null && onGroupClickListener != null) {
                onGroupClickListener.onResult(news.getGroup());
            } else if (news.getUser() != null && onUserClickListener != null) {
                onUserClickListener.onResult(news.getUser());
            }
        }
    }
}
