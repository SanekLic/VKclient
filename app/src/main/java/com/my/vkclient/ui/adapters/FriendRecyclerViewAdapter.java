package com.my.vkclient.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

public abstract class FriendRecyclerViewAdapter extends BaseRecyclerViewAdapter<User> {

    public FriendRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        super(linearLayoutManager);
    }

    @Override
    BaseViewHolder<User> getViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_friend, parent, false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final BaseViewHolder<User> holder) {
        super.onViewAttachedToWindow(holder);

        if (validateLoadMoreItems()) {
            loadMoreItems(totalItemCount, PAGE_SIZE);
        }
    }

    public void initialLoadItems() {
        loadMoreItems(0, PAGE_SIZE * 2);
    }

//    private void setItems(List<User> newFriendList) {
//        userDiffUtilCallback
//                .setOldList(friendList)
//                .setNewList(newFriendList);
//        friendList.clear();
//        friendList.addAll(newFriendList);
//        DiffUtil.calculateDiff(userDiffUtilCallback).dispatchUpdatesTo(this);
//    }

    public abstract void load(final int startPosition, final int size, ResultCallback<List<User>> listResultCallback);

    private void loadMoreItems(final int startPosition, final int size) {
        isLoading = true;

        load(startPosition, size, new ResultCallback<List<User>>() {
            @Override
            public void onResult(final List<User> resultList) {
                if (resultList != null) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultList.size() < size) {
                                setLoadComplete();
                            }

                            addItems(resultList);
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

