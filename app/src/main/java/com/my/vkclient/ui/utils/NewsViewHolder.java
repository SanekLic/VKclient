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
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;

class NewsViewHolder extends RecyclerView.ViewHolder {
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());

    private Context context;
    private ImageView sourceIconImageView;
    private TextView sourceNameTextView;
    private TextView newsDateTextView;
    private TextView newsTextView;
    private RecyclerView attachmentRecyclerView;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;

    public NewsViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        sourceIconImageView = itemView.findViewById(R.id.sourceIconImageView);
        sourceNameTextView = itemView.findViewById(R.id.sourceNameTextView);
        newsDateTextView = itemView.findViewById(R.id.newsDateTextView);
        newsTextView = itemView.findViewById(R.id.newsTextView);
        attachmentRecyclerView = itemView.findViewById(R.id.attachmentRecyclerView);

        setupAttachmentRecyclerView();
    }

    public void bind(News news) {
        newsDateTextView.setText(news.getDate().toString());
        newsTextView.setText(news.getText());

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

        attachmentRecyclerViewAdapter.setItems(news.getAttachments());
    }

    private void setupAttachmentRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(linearLayoutManager);
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
