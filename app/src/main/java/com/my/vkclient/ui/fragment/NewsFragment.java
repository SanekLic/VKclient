package com.my.vkclient.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.my.vkclient.Constants;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.activity.ImageActivity;
import com.my.vkclient.ui.activity.UserActivity;
import com.my.vkclient.ui.adapters.NewsRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

import static com.my.vkclient.Constants.IntentKey.GROUP_ID_INTENT_KEY;

public class NewsFragment extends BaseFragment<NewsRecyclerViewAdapter, News> {

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
        recyclerViewAdapter.setOnUserClickListener(new ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                UserActivity.show(NewsFragment.this.getContext(), user.getId());
            }
        });
        recyclerViewAdapter.setOnGroupClickListener(new ResultCallback<Group>() {
            @Override
            public void onResult(Group group) {
                if (getActivity() != null) {
                    GroupInfoDialogFragment groupInfoDialogFragment = new GroupInfoDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(GROUP_ID_INTENT_KEY, group.getId());
                    groupInfoDialogFragment.setArguments(bundle);
                    groupInfoDialogFragment.show(getActivity().getSupportFragmentManager(), Constants.DialogFragment.GROUP_INFO_DIALOG_FRAGMENT_TAG);
                }
            }
        });
    }
}
