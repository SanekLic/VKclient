package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.Photo;

public abstract class UserPhotoRecyclerViewAdapter extends RecyclerView.Adapter<UserPhotoViewHolder> {
    private static final int PAGE_SIZE = 10;

    protected UserPhotoRecyclerViewAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super(context, linearLayoutManager);
    }

    @Override
    protected BaseViewHolder<Photo> getViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_user_photo, parent, false);

        return new UserPhotoViewHolder(view);
    }

    int getPageSize() {
        return PAGE_SIZE;
    }
}

