package com.my.vkclient.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.activity.ImageActivity;
import com.my.vkclient.ui.adapters.NewsRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

import static com.my.vkclient.Constants.RecyclerView.IS_LOAD_COMPLETE_STATE_KEY;
import static com.my.vkclient.Constants.RecyclerView.LINEAR_LAYOUT_MANAGER_STATE_KEY;
import static com.my.vkclient.Constants.RecyclerView.NEXT_FROM_STATE_KEY;

public class NewsFragment extends Fragment {

    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupRecyclerView(view);
        setupSwipeRefresh(view);

        if (savedInstanceState != null) {
            newsRecyclerViewAdapter.addItems(VkRepository.getInstance().getNewsList());
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY));
            newsRecyclerViewAdapter.setNextFrom(savedInstanceState.getString(NEXT_FROM_STATE_KEY));

            if (savedInstanceState.getBoolean(IS_LOAD_COMPLETE_STATE_KEY)) {
                newsRecyclerViewAdapter.setLoadComplete();
            }
        } else {
            newsRecyclerViewAdapter.initialLoadItems();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY, linearLayoutManager.onSaveInstanceState());
        outState.putBoolean(IS_LOAD_COMPLETE_STATE_KEY, newsRecyclerViewAdapter.isLoadComplete());
        outState.putString(NEXT_FROM_STATE_KEY, newsRecyclerViewAdapter.getNextFrom());
        VkRepository.getInstance().setNewsList(newsRecyclerViewAdapter.getItemList());
    }

    private void setupSwipeRefresh(@NonNull View view) {
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh);
        swipeRefresh.isRefreshing();
    }

    private void setupRecyclerView(View view) {
        final RecyclerView newsRecyclerView = view.findViewById(R.id.newsRecyclerView);

        newsRecyclerView.addItemDecoration(new android.support.v7.widget.RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull android.support.v7.widget.RecyclerView parent, @NonNull android.support.v7.widget.RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Constants.RecyclerView.VIEW_MARGIN;
                }

                outRect.bottom = Constants.RecyclerView.VIEW_MARGIN;
                outRect.left = Constants.RecyclerView.VIEW_MARGIN;
                outRect.right = Constants.RecyclerView.VIEW_MARGIN;
            }
        });
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(linearLayoutManager) {
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
        newsRecyclerViewAdapter.setOnAttachmentClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        newsRecyclerViewAdapter.setOnPhotoClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    ImageActivity.show(NewsFragment.this.getContext(), result);
                }
            }
        });

        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);
    }
}
