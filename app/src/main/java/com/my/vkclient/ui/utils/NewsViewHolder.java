package com.my.vkclient.ui.utils;

import android.os.Handler;
import android.os.Looper;
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

    private ImageView sourceIconImageView;
    private TextView sourceNameTextView;
    private TextView newsDateTextView;
    private TextView newsTextView;

    public NewsViewHolder(View itemView) {
        super(itemView);

        sourceIconImageView = itemView.findViewById(R.id.sourceIconImageView);
        sourceNameTextView = itemView.findViewById(R.id.sourceNameTextView);
        newsDateTextView = itemView.findViewById(R.id.newsDateTextView);
        newsTextView = itemView.findViewById(R.id.newsTextView);
    }

    public void bind(News news) {
        newsDateTextView.setText(String.valueOf(news.getDate()));
        newsTextView.setText(news.getText());

        if (news.getSourceId() < 0) {
            VkRepository.getGroupById(news.getSourceId() * (-1), new ResultCallback<Group>() {
                @Override
                public void onResult(final Group result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            sourceNameTextView.setText(result.getName());
                            ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100());
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
                            sourceNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(" ").append(result.getLastName()).toString());
                            ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100());
                        }
                    });
                }
            });
        }
    }
}
