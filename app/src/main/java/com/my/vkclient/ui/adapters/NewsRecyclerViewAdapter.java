package com.my.vkclient.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.utils.ResultCallback;

import static com.my.vkclient.Constants.STRING_EMPTY;

public abstract class NewsRecyclerViewAdapter extends BaseRecyclerViewAdapter<News> {

    private static final int PAGE_SIZE = 5;
    private String nextFrom = STRING_EMPTY;

    public NewsRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        super(linearLayoutManager);
    }

    @Override
    BaseViewHolder<News> getViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_news, parent, false);

        return new NewsViewHolder(parent.getContext(), view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final BaseViewHolder<News> holder) {
        super.onViewAttachedToWindow(holder);

        if (validateLoadMoreItems()) {
            loadMoreItems(nextFrom, PAGE_SIZE);
        }
    }

    public void initialLoadItems() {
        loadMoreItems(nextFrom, PAGE_SIZE * 2);
    }

    protected abstract void load(final String startPosition, final int size, ResultCallback<NewsResponse.Response> listResultCallback);

    private void loadMoreItems(final String startPosition, final int size) {
        isLoading = true;

        load(startPosition, size, new ResultCallback<NewsResponse.Response>() {
            @Override
            public void onResult(final NewsResponse.Response resultList) {
                if (resultList != null) {
                    nextFrom = resultList.getNextFrom();

                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultList.getNewsList().size() < size) {
                                setLoadComplete();
                            }

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

