package com.my.vkclient.ui.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.ResultCallback;
import com.my.vkclient.VkRepository;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.User;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private static final int PAGE_SIZE = 20;
    private static final long INTERVAL_MILLIS = 5000;
    private List<News> newsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLoadComplete;
    private NewsDiffUtilCallback newsDiffUtilCallback;
    private boolean runAutoUpdate = false;
    private Thread friendListUpdateThread;
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private ResultCallback<User> itemClickListener;
    private String nextFrom = "";

    public NewsRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        newsDiffUtilCallback = new NewsDiffUtilCallback();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_view, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final NewsViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        validateLoadMoreItems();
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void initialLoadItems() {
        loadMoreItems(nextFrom, PAGE_SIZE * 2);
    }

    private void setItems(List<News> newNewsList) {
        newsDiffUtilCallback
                .setOldList(newsList)
                .setNewList(newNewsList);
        newsList.clear();
        newsList.addAll(newNewsList);
        DiffUtil.calculateDiff(newsDiffUtilCallback).dispatchUpdatesTo(this);
    }

    private void addItems(List<News> addNewsList) {
        newsList.addAll(addNewsList);
        notifyItemRangeInserted(newsList.size() - addNewsList.size(), addNewsList.size());
    }

    private void validateLoadMoreItems() {
        if (!isLoading && !isLoadComplete) {
            int totalItemCount = linearLayoutManager.getItemCount();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition + PAGE_SIZE) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems(nextFrom, PAGE_SIZE);
            }
        }
    }

    private void loadMoreItems(final String startPosition, final int size) {
        isLoading = true;

        VkRepository.getNews(startPosition, size, new ResultCallback<NewsResponse.Response>() {
            @Override
            public void onResult(final NewsResponse.Response resultList) {
                if (resultList != null) {
                    if (resultList.getNewsList().size() < size) {
                        isLoadComplete = true;
                    }

                    nextFrom = resultList.getNextFrom();

                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            addItems(resultList.getNewsList());
                            isLoading = false;
                        }
                    });
                } else {
                    isLoadComplete = true;
                    isLoading = false;
                }
            }
        });
    }
}

