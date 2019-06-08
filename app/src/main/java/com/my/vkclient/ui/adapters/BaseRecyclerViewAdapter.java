package com.my.vkclient.ui.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.utils.ResultCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    static final int PAGE_SIZE = 20;
    Handler mainLooperHandler;
    boolean isLoading;
    int totalItemCount;
    private boolean isLoadComplete;
    private LinearLayoutManager linearLayoutManager;
    private List<T> itemList = new ArrayList<>();
    private ResultCallback<T> itemClickListener;

    BaseRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        mainLooperHandler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, @ViewType int viewType) {
        if (viewType == ViewType.LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progress, parent, false);

            return new BaseViewHolder<T>(view) {
                @Override
                public void bind(T data) {
                }
            };
        } else {
            final BaseViewHolder<T> holder = getViewHolder(parent, viewType);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onResult(getItem(position));
                        }
                    }
                }
            });

            return holder;
        }
    }

    abstract BaseViewHolder<T> getViewHolder(@NonNull ViewGroup parent, @ViewType int viewType);

    public void setOnItemClickListener(ResultCallback<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    boolean validateLoadMoreItems() {
        if (!isLoading && !isLoadComplete) {
            totalItemCount = linearLayoutManager.getItemCount();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            return (visibleItemCount + firstVisibleItemPosition + PAGE_SIZE) >= totalItemCount
                    && firstVisibleItemPosition >= 0;
        }

        return false;
    }

    T getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemCount() {
        if (isLoadComplete) {
            return itemList.size();
        } else {
            return itemList.size() + 1;
        }
    }

    void setLoadComplete() {
        isLoadComplete = true;
        notifyItemRemoved(itemList.size());
    }

    void addItems(List<T> addFriendList) {
        itemList.addAll(addFriendList);
        int positionStart = itemList.size() - addFriendList.size();

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart, addFriendList.size());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        if (getItemViewType(position) != ViewType.LOADING) {
            holder.bind(getItem(position));
        }
    }

    @ViewType
    @Override
    public int getItemViewType(final int position) {
        if (position < itemList.size()) {
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
}

