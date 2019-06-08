package com.my.vkclient.ui.adapters;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @ViewType int viewType) {
        if (viewType == ViewType.LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_progress, parent, false);
            return new ViewHolder(view);
        } else {
            return getViewHolder(parent, viewType);
        }
    }

    abstract BaseViewHolder getViewHolder(@NonNull ViewGroup parent, @ViewType int viewType);

    abstract int getListItemCount();

    abstract T getItem(int position);

    @Override
    public int getItemCount() {
        return getListItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getItemViewType(position) != ViewType.LOADING) {
            holder.bind(getItem(position));
        }
    }

    @ViewType
    @Override
    public int getItemViewType(final int position) {
        if (position < getListItemCount()) {
            return ViewType.ANY;
        } else {
            return ViewType.LOADING;
        }
    }

    @IntDef({ViewType.LOADING, ViewType.ANY})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
        int LOADING = 0;
        int ANY = 1;
    }

    public class ViewHolder extends BaseViewHolder {

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Object data) {

        }
    }
}

