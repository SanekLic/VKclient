package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.ui.adapters.NewsRecyclerViewAdapter;

public class NewsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView newsRecyclerView = view.findViewById(R.id.newsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        NewsRecyclerViewAdapter newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(linearLayoutManager);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        newsRecyclerViewAdapter.initialLoadItems();
    }
}
