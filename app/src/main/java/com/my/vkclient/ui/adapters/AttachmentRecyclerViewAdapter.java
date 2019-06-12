package com.my.vkclient.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.utils.ResultCallback;

import java.util.ArrayList;
import java.util.List;

public class AttachmentRecyclerViewAdapter extends RecyclerView.Adapter<AttachmentViewHolder> {

    private List<Attachment> attachmentList = new ArrayList<>();
    private ResultCallback<String> onAttachmentClickListener;
    private ResultCallback<String> onPhotoClickListener;

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_attachment, parent, false);

        AttachmentViewHolder holder = new AttachmentViewHolder(parent.getContext(), view);
        holder.setOnAttachmentClickListener(onAttachmentClickListener);
        holder.setOnPhotoClickListener(onPhotoClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        holder.bind(attachmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    void setOnAttachmentClickListener(ResultCallback<String> onAttachmentClickListener) {
        this.onAttachmentClickListener = onAttachmentClickListener;
    }

    void setOnPhotoClickListener(ResultCallback<String> onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    void setItems(List<Attachment> newAttachmentList) {
        attachmentList.clear();
        attachmentList.addAll(newAttachmentList);
        notifyDataSetChanged();
    }
}

