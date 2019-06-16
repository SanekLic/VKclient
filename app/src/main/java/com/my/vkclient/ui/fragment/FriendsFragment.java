package com.my.vkclient.ui.fragment;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.my.vkclient.Constants;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.activity.UserActivity;
import com.my.vkclient.ui.adapters.FriendRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

public class FriendsFragment extends BaseFragment<FriendRecyclerViewAdapter, User> {

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new android.support.v7.widget.RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull android.support.v7.widget.RecyclerView parent, @NonNull android.support.v7.widget.RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Constants.RecyclerView.VIEW_MARGIN;
                }
            }
        };
    }

    @Override
    void refreshRepository() {
        VkRepository.getInstance().refreshFriends();
    }

    @Override
    void createRecyclerViewAdapter() {
        recyclerViewAdapter = new FriendRecyclerViewAdapter(this.getContext(), linearLayoutManager) {
            @Override
            public void load(int startPosition, int size, ResultCallback<List<User>> listResultCallback) {
                VkRepository.getInstance().getFriends(startPosition, size, listResultCallback);
            }

            @Override
            public void onLoadStateChanged(boolean state) {
                swipeRefresh.setEnabled(!state);
            }
        };
        recyclerViewAdapter.setOnItemClickListener(new ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                UserActivity.show(FriendsFragment.this.getContext(), user.getId());
            }
        });
    }

    @Override
    List<User> getItemsFromRepository() {
        return VkRepository.getInstance().getFriendList();
    }
}
