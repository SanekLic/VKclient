package com.my.vkclient.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.my.vkclient.R;
import com.my.vkclient.ui.utils.NewsRecyclerViewAdapter;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecyclerView();
    }

    @Override
    protected int getView() {
        return R.layout.activity_news;
    }

    private void setupRecyclerView() {
        RecyclerView newsRecyclerView = findViewById(R.id.newsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        NewsRecyclerViewAdapter newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(linearLayoutManager);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        newsRecyclerViewAdapter.initialLoadItems();
    }
}
