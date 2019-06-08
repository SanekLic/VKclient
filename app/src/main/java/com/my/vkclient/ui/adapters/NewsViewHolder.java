package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
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
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

class NewsViewHolder extends BaseViewHolder<News> {
    private final TextView likesTextView;
    private final TextView commentsTextView;
    private final TextView repostsTextView;
    private final TextView viewsTextView;
    private final ImageView sourceIconImageView;
    private final TextView sourceNameTextView;
    private final TextView sourceNewsDateTextView;
    private final ImageView fromIconImageView;
    private final TextView fromNameTextView;
    private final TextView fromNewsDateTextView;
    private final TextView newsTextView;
    private final ImageView newsPhotoImageView;
    private final RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;

    public NewsViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
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
        commentsTextView = itemView.findViewById(R.id.commentsTextView);
        repostsTextView = itemView.findViewById(R.id.repostsTextView);
        viewsTextView = itemView.findViewById(R.id.viewsTextView);

        setupAttachmentRecyclerView();
    }

    public void bind(News news) {
        sourceNewsDateTextView.setText(Utils.getInstance().getSimpleDate(news.getDate()));

        if (news.getSourceId() < 0) {
            VkRepository.getInstance().getGroup(news.getSourceId() * (-1), new ResultCallback<Group>() {
                @Override
                public void onResult(final Group result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(result.getName());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getInstance().getImageFromUrl(sourceIconImageView, result.getPhoto100(), 0, 0);
                            }
                        }
                    });
                }
            });
        } else {
            VkRepository.getInstance().getUser(news.getSourceId(), new ResultCallback<User>() {
                @Override
                public void onResult(final User result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(Constants.STRING_SPACE).append(result.getLastName()).toString());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getInstance().getImageFromUrl(sourceIconImageView, result.getPhoto100Url(), 0, 0);
                            }
                        }
                    });
                }
            });
        }

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(news.getCopyHistory().size() - 1);
            fromNewsDateTextView.setText(Utils.getInstance().getSimpleDate(newsCopy.getDate()));

            if (newsCopy.getFromId() < 0) {
                VkRepository.getInstance().getGroup(newsCopy.getFromId() * (-1), new ResultCallback<Group>() {
                    @Override
                    public void onResult(final Group result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(result.getName());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getInstance().getImageFromUrl(fromIconImageView, result.getPhoto100(), 0, 0);
                                }
                            }
                        });
                    }
                });
            } else {
                VkRepository.getInstance().getUser(newsCopy.getFromId(), new ResultCallback<User>() {
                    @Override
                    public void onResult(final User result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(Constants.STRING_SPACE).append(result.getLastName()).toString());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getInstance().getImageFromUrl(fromIconImageView, result.getPhoto100Url(), 0, 0);
                                }
                            }
                        });
                    }
                });
            }

            setNewsText(newsCopy.getText());
            setAttachments(newsCopy.getAttachments());
            setVisibilityCopyNews(View.VISIBLE);
        } else {
            setNewsText(news.getText());
            setAttachments(news.getAttachments());
            setVisibilityCopyNews(View.GONE);
        }

        if (news.getLikes().getUserLikes()) {
            likesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this.context, R.drawable.ic_fill_likes), null, null, null);
        } else{
            likesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this.context, R.drawable.ic_likes), null, null, null);
        }

        likesTextView.setText(Utils.getInstance().formatNumber(news.getLikes().getCount()));
        commentsTextView.setText(Utils.getInstance().formatNumber(news.getComments().getCount()));
        repostsTextView.setText(Utils.getInstance().formatNumber(news.getReposts().getCount()));

        if (news.getViews() != null) {
            viewsTextView.setText(Utils.getInstance().formatNumber(news.getViews().getCount()));
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
