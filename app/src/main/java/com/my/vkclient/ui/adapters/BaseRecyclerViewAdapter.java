package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnimRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.my.vkclient.R;
import com.my.vkclient.utils.ResultCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private final Context context;
    private int previousPosition = 0;
    private int pageSize;
    private Handler mainLooperHandler;
    private boolean isLoading;
    private boolean isLoadComplete;
    private boolean isAnimationEnable = true;
    private LinearLayoutManager linearLayoutManager;
    private List<T> itemList = new ArrayList<>();
    private ResultCallback<T> itemClickListener;
    private int layoutProgressResource;

    BaseRecyclerViewAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        this.context = context;
        this.linearLayoutManager = linearLayoutManager;

        if (this.linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            layoutProgressResource = R.layout.layout_progress_horizontal;
        } else {
            layoutProgressResource = R.layout.layout_progress_vertical;
        }

        mainLooperHandler = new Handler(Looper.getMainLooper());
        pageSize = getPageSize();
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, @ViewType int viewType) {
        if (viewType == ViewType.LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutProgressResource, parent, false);

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

    @Override
    public void onViewAttachedToWindow(@NonNull final BaseViewHolder<T> holder) {
        super.onViewAttachedToWindow(holder);

        if (validateLoadMoreItems()) {
            loadMoreItems(itemList.size(), pageSize);
        }

        if (isAnimationEnable) {
            if (previousPosition < holder.getAdapterPosition()) {
                holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, getAnimationToNextResource()));
            } else {
                holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, getAnimationToPreviousResource()));
            }
        }

        previousPosition = holder.getAdapterPosition();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        if (getItemViewType(position) != ViewType.LOADING) {
            holder.bind(getItem(position));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder<T> holder) {
        super.onViewDetachedFromWindow(holder);

        if (isAnimationEnable) {
            holder.itemView.clearAnimation();
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

    @Override
    public int getItemCount() {
        if (isLoadComplete) {
            return itemList.size();
        } else {
            return itemList.size() + 1;
        }
    }

    public void setOnItemClickListener(ResultCallback<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public abstract void load(final int startPosition, final int size, ResultCallback<List<T>> listResultCallback);

    public abstract void onLoadStateChanged(boolean state);

    public void initialLoadItems() {
        loadMoreItems(0, pageSize * 2);
    }

    public void setLoadComplete() {
        isLoadComplete = true;
        notifyItemRemoved(itemList.size());
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }

    public void addItems(List<T> newItemList) {
        itemList.addAll(newItemList);
        int positionInsert = itemList.size() <= newItemList.size() ? 0 : itemList.size() - newItemList.size();

        if (positionInsert == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionInsert, newItemList.size());
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        onLoadStateChanged(isLoading);
    }

    public void refreshItems() {
        itemList.clear();
        isLoadComplete = false;
        initialLoadItems();
        notifyDataSetChanged();
    }

    public void setAnimationEnabled(boolean enable) {
        isAnimationEnable = enable;
    }

    public @AnimRes
    int getAnimationToNextResource() {
        return R.anim.item_translate_up_anim;
    }

    public @AnimRes
    int getAnimationToPreviousResource() {
        return R.anim.item_translate_down_anim;
    }

    protected abstract BaseViewHolder<T> getViewHolder(@NonNull ViewGroup parent, @ViewType int viewType);

    abstract int getPageSize();

    private void loadMoreItems(final int startPosition, final int size) {
        setLoading(true);

        load(startPosition, size, new ResultCallback<List<T>>() {
            @Override
            public void onResult(final List<T> resultList) {
                if (resultList != null) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultList.size() < size) {
                                setLoadComplete();
                            }

                            addItems(resultList);
                            setLoading(false);
                        }
                    });
                } else {
                    setLoading(false);
                }
            }
        });
    }

    private boolean validateLoadMoreItems() {
        if (!isLoading && !isLoadComplete) {
            int totalItemCount = linearLayoutManager.getItemCount();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            return (visibleItemCount + firstVisibleItemPosition + pageSize) >= totalItemCount
                    && firstVisibleItemPosition >= 0;
        }

        return false;
    }

    private T getItem(int position) {
        return itemList.get(position);
    }

    @IntDef({ViewType.LOADING, ViewType.ANY})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
        int LOADING = 0;
        int ANY = 1;
    }
}

