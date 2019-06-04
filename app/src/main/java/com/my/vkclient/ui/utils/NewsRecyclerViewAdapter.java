package com.my.vkclient.ui.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.Repository.VkRepository;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.utils.ResultCallback;

import java.util.ArrayList;
import java.util.List;

import static com.my.vkclient.Constants.STRING_EMPTY;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private static final int PAGE_SIZE = 20;
    private List<News> newsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLoadComplete;
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private String nextFrom = STRING_EMPTY;

    public NewsRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_news, parent, false);

        return new NewsViewHolder(parent.getContext(), view);
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


    //TODO move this logic out from adapter
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
                    isLoading = false;
                }
            }
        });
    }
}

