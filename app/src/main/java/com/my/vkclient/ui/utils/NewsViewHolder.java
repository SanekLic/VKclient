package com.my.vkclient.ui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.ResultCallback;
import com.my.vkclient.VkRepository;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.Size;
import com.my.vkclient.entities.User;

import java.util.ArrayList;
import java.util.List;

class NewsViewHolder extends RecyclerView.ViewHolder {
    List<Attachment> attachments = new ArrayList<>();
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private ImageView sourceIconImageView;
    private TextView sourceNameTextView;
    private TextView sourceNewsDateTextView;
    private ImageView fromIconImageView;
    private TextView fromNameTextView;
    private TextView fromNewsDateTextView;
    private TextView newsTextView;
    private ImageView newsPhotoImageView;
    private RecyclerView attachmentRecyclerView;
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

        setupAttachmentRecyclerView();
    }

    public void bind(News news) {
        sourceNewsDateTextView.setText(news.getDate().toString());

        if (news.getSourceId() < 0) {
            VkRepository.getGroupById(news.getSourceId() * (-1), new ResultCallback<Group>() {
                @Override
                public void onResult(final Group result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(result.getName());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100());
                            }
                        }
                    });
                }
            });
        } else {
            VkRepository.getUserById(news.getSourceId(), new ResultCallback<User>() {
                @Override
                public void onResult(final User result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(" ").append(result.getLastName()).toString());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100());
                            }
                        }
                    });
                }
            });
        }

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(news.getCopyHistory().size() - 1);
            fromNewsDateTextView.setText(newsCopy.getDate().toString());

            if (newsCopy.getFromId() < 0) {
                VkRepository.getGroupById(newsCopy.getFromId() * (-1), new ResultCallback<Group>() {
                    @Override
                    public void onResult(final Group result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(result.getName());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getImageFromUrl(fromIconImageView, result.getPhoto100());
                                }
                            }
                        });
                    }
                });
            } else {
                VkRepository.getUserById(newsCopy.getFromId(), new ResultCallback<User>() {
                    @Override
                    public void onResult(final User result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(" ").append(result.getLastName()).toString());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getImageFromUrl(fromIconImageView, result.getPhoto100());
                                }
                            }
                        });
                    }
                });
            }

            newsTextView.setText(newsCopy.getText());
            setAttachments(newsCopy.getAttachments());

            if (fromIconImageView.getVisibility() == View.GONE) {
                fromIconImageView.setVisibility(View.VISIBLE);
                fromNameTextView.setVisibility(View.VISIBLE);
                fromNewsDateTextView.setVisibility(View.VISIBLE);
            }

        } else {
            newsTextView.setText(news.getText());
            setAttachments(news.getAttachments());

            if (fromIconImageView.getVisibility() == View.VISIBLE) {
                fromIconImageView.setVisibility(View.GONE);
                fromNameTextView.setVisibility(View.GONE);
                fromNewsDateTextView.setVisibility(View.GONE);
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
                    int maxWidth = 0;
                    String urlPhoto = attachment.getPhoto().getSizes().get(0).getUrl();

                    for (Size size : attachment.getPhoto().getSizes()) {
                        if (size.getWidth() > maxWidth) {
                            maxWidth = size.getWidth();
                            urlPhoto = size.getUrl();
                        }
                    }

                    attachments.remove(i);
                    ImageLoader.getImageFromUrl(newsPhotoImageView, urlPhoto);

                    break;
                }
            }
        }

        attachmentRecyclerViewAdapter.setItems(attachments);
    }

    private void setupAttachmentRecyclerView() {
        attachmentRecyclerView = itemView.findViewById(R.id.attachmentRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(linearLayoutManager);
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
