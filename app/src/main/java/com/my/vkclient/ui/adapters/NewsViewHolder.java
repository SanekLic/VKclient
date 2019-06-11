package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
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

import static com.my.vkclient.Constants.AUDIO_FORMAT;
import static com.my.vkclient.Constants.STRING_EMPTY;

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
    private TextView newsAttachmentInfoTextView;
    private TextView newsAttachmentTypeTextView;
    private RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;
    private News news;
    private ResultCallback<News> onLikeClickListener;
    private ResultCallback<String> onAttachmentClickListener;
    private ResultCallback<String> onPhotoClickListener;
    private String contentUrl;
    private Attachment attachment;

    NewsViewHolder(Context context, View itemView) {
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
        newsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                i++;
                newsTextView.setMaxLines(64);
            }
        });
        newsPhotoImageView = itemView.findViewById(R.id.newsPhotoImageView);
        newsPhotoImageView.setOnClickListener(new View.OnClickListener() {
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
        newsAttachmentInfoTextView = itemView.findViewById(R.id.newsAttachmentInfoTextView);
        newsAttachmentTypeTextView = itemView.findViewById(R.id.newsAttachmentTypeTextView);
    }

    void setOnLikeClickListener(ResultCallback<News> onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        this.onAttachmentClickListener = onAttachmentClickListener;
    }

    void setOnPhotoClickListener(ResultCallback<String> onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
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

    private void setTextAndVisibilityTextView(TextView textView, String text) {
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

    private void setAttachments(List<Attachment> newAttachments) {
        newsPhotoImageView.setImageDrawable(null);
        attachments.clear();

        if (newAttachments != null) {
            attachments.addAll(newAttachments);

            for (int i = 0; i < attachments.size(); i++) {
                attachment = attachments.get(i);

                if (Attachment.Type.Photo.equals(attachment.getType())) {
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, STRING_EMPTY);
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, "Фото");
                    contentUrl = STRING_EMPTY;
                    setAttachmentImage(attachment.getPhoto());
                } else if (Attachment.Type.Doc.equals(attachment.getType())) {
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, attachment.getDoc().getTitle());
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, attachment.getDoc().getExt());
                    contentUrl = attachment.getDoc().getUrl();

                    if (attachment.getDoc().getPhotoUrl() == null) {
                        newsPhotoImageView.setImageResource(R.drawable.ic_attachment_doc);
                    } else {
                        setAttachmentImage(attachment.getDoc());
                    }
                } else if (Attachment.Type.Video.equals(attachment.getType())) {
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, attachment.getVideo().getTitle());
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, "Видео");
                    contentUrl = STRING_EMPTY;
                    setAttachmentImage(attachment.getVideo());
                } else if (Attachment.Type.Link.equals(attachment.getType())) {
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, attachment.getLink().getTitle());
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, "Ссылка");
                    contentUrl = attachment.getLink().getUrl();

                    if (attachment.getLink().getPhotoUrl() == null) {
                        newsPhotoImageView.setImageResource(R.drawable.ic_attachment_link);
                    } else {
                        setAttachmentImage(attachment.getLink());
                    }
                } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, attachment.getPodcast().getTitle());
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, "Подкаст");
                    contentUrl = attachment.getPodcast().getUrl();
                    setAttachmentImage(attachment.getPodcast());
                } else if (Attachment.Type.Audio.equals(attachment.getType())) {
                    String info = attachment.getAudio().getArtist() != null ?
                            String.format(AUDIO_FORMAT, attachment.getAudio().getArtist(), attachment.getAudio().getTitle()) :
                            attachment.getAudio().getTitle();
                    setTextAndVisibilityTextView(newsAttachmentInfoTextView, info);
                    setTextAndVisibilityTextView(newsAttachmentTypeTextView, "Аудио");
                    contentUrl = STRING_EMPTY;
                    newsPhotoImageView.setImageResource(R.drawable.ic_attachment_audio);
                } else {
                    continue;
                }

                attachments.remove(i);

                break;
            }
        }

        attachmentRecyclerViewAdapter.setItems(attachments);
    }

    private void setAttachmentImage(AttachmentPhoto attachmentPhoto) {
        if (attachmentPhoto != null) {
            ImageLoader.getInstance().getImageFromUrl(newsPhotoImageView, attachmentPhoto.getPhotoUrl(),
                    attachmentPhoto.getPhotoWidth(), attachmentPhoto.getPhotoHeight());
        } else {
            newsPhotoImageView.setImageDrawable(null);
        }
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
        attachmentRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = Constants.AttachmentRecyclerView.ATTACHMENT_MARGIN;
                }
            }
        });
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerViewAdapter.setOnAttachmentClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (onAttachmentClickListener != null) {
                    onAttachmentClickListener.onResult(result);
                }
            }
        });
        attachmentRecyclerViewAdapter.setOnPhotoClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (onPhotoClickListener != null) {
                    onPhotoClickListener.onResult(result);
                }
            }
        });
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
