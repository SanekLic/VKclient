package com.my.vkclient.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.my.vkclient.R;
import com.my.vkclient.ui.utils.NewsRecyclerViewAdapter;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(linearLayoutManager);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        newsRecyclerViewAdapter.initialLoadItems();
//        newsRecyclerViewAdapter.autoUpdateItems(true);
    }
}
