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
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.adapters.NewsRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

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
        final RecyclerView newsRecyclerView = view.findViewById(R.id.newsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        final NewsRecyclerViewAdapter newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(linearLayoutManager) {
            @Override
            public void load(String startPosition, int size, ResultCallback<NewsResponse.Response> listResultCallback) {
                VkRepository.getInstance().getNews(startPosition, size, listResultCallback);
            }
        };
        newsRecyclerViewAdapter.setOnLikeClickListener(new ResultCallback<News>() {
            @Override
            public void onResult(News news) {
                VkRepository.getInstance().setLikeToNews(news, !news.getLikes().getUserLikes(), new ResultCallback<News>() {
                    @Override
                    public void onResult(News result) {
                        newsRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                newsRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        newsRecyclerViewAdapter.initialLoadItems();
    }
}
