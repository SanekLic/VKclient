package com.my.vkclient.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.News;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.activity.ImageActivity;
import com.my.vkclient.ui.adapters.NewsRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

public class NewsFragment extends BaseFragment<NewsRecyclerViewAdapter, News> {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new android.support.v7.widget.RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull android.support.v7.widget.RecyclerView parent, @NonNull android.support.v7.widget.RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Constants.RecyclerView.VIEW_MARGIN;
                }

                outRect.bottom = Constants.RecyclerView.VIEW_MARGIN;
                outRect.left = Constants.RecyclerView.VIEW_MARGIN;
                outRect.right = Constants.RecyclerView.VIEW_MARGIN;
            }
        };
    }

    @Override
    List<News> getItemsFromRepository() {
        return VkRepository.getInstance().getNewsList();
    }

    @Override
    void refreshRepository() {
        VkRepository.getInstance().refreshNews();
    }

    @Override
    void createRecyclerViewAdapter() {
        recyclerViewAdapter = new NewsRecyclerViewAdapter(this.getContext(), linearLayoutManager) {
            @Override
            public void load(int startPosition, int size, ResultCallback<List<News>> listResultCallback) {
                VkRepository.getInstance().getNews(startPosition, size, listResultCallback);
            }

            @Override
            public void onLoadStateChanged(boolean state) {
                swipeRefresh.setEnabled(!state);
            }
        };
        recyclerViewAdapter.setOnLikeClickListener(new ResultCallback<News>() {
            @Override
            public void onResult(News news) {
                VkRepository.getInstance().setLikeToNews(news, !news.getLikes().getUserLikes(), new ResultCallback<News>() {
                    @Override
                    public void onResult(News result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
        recyclerViewAdapter.setOnAttachmentClickListener(new ResultCallback<String>() {
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
        recyclerViewAdapter.setOnPhotoClickListener(new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    ImageActivity.show(NewsFragment.this.getContext(), result);
                }
            }
        });
    }
}
