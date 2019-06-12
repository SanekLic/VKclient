package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.News;
import com.my.vkclient.utils.ResultCallback;

public abstract class NewsRecyclerViewAdapter extends BaseRecyclerViewAdapter<News> {
    private static final int PAGE_SIZE = 10;
    private ResultCallback<News> onLikeClickListener;
    private ResultCallback<String> onAttachmentClickListener;
    private ResultCallback<String> onPhotoClickListener;

    protected NewsRecyclerViewAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super(context, linearLayoutManager);
    }

    @Override
    protected BaseViewHolder<News> getViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_news, parent, false);

        final NewsViewHolder holder = new NewsViewHolder(parent.getContext(), view);
        holder.setOnLikeClickListener(onLikeClickListener);
        holder.setOnAttachmentClickListener(onAttachmentClickListener);
        holder.setOnPhotoClickListener(onPhotoClickListener);

        return holder;
    }

    public void setOnLikeClickListener(ResultCallback<News> onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    public void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        this.onAttachmentClickListener = onAttachmentClickListener;
    }

    public void setOnPhotoClickListener(ResultCallback<String> onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    int getPageSize() {
        return PAGE_SIZE;
    }
}

