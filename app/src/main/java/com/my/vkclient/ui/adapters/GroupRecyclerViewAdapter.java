package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.User;

public abstract class GroupRecyclerViewAdapter extends BaseRecyclerViewAdapter<Group> {
    private static final int PAGE_SIZE = 20;

    protected GroupRecyclerViewAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super(context, linearLayoutManager);
    }

    @Override
    protected BaseViewHolder<Group> getViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_group, parent, false);

        return new GroupViewHolder(view);
    }

    int getPageSize() {
        return PAGE_SIZE;
    }
}

