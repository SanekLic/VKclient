package com.my.vkclient.ui.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.R;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.Repository.VkRepository;
import com.my.vkclient.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private static final int PAGE_SIZE = 20;
    private List<User> friendList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLoadComplete;
    private UserDiffUtilCallback userDiffUtilCallback;
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private ResultCallback<User> itemClickListener;

    public FriendRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        userDiffUtilCallback = new UserDiffUtilCallback();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_friend, parent, false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final FriendViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onResult(friendList.get(position));
                    }
                }
            }
        });
        validateLoadMoreItems();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friendList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void initialLoadItems() {
        loadMoreItems(0, PAGE_SIZE * 2);
    }

    public void setOnItemClickListener(ResultCallback<User> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setItems(List<User> newFriendList) {
        userDiffUtilCallback
                .setOldList(friendList)
                .setNewList(newFriendList);
        friendList.clear();
        friendList.addAll(newFriendList);
        DiffUtil.calculateDiff(userDiffUtilCallback).dispatchUpdatesTo(this);
    }

    private void addItems(List<User> addFriendList) {
        friendList.addAll(addFriendList);
        notifyItemRangeInserted(friendList.size() - addFriendList.size(), addFriendList.size());
    }

    private void validateLoadMoreItems() {
        if (!isLoading && !isLoadComplete) {
            int totalItemCount = linearLayoutManager.getItemCount();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition + PAGE_SIZE) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems(totalItemCount, PAGE_SIZE);
            }
        }
    }

    private void loadMoreItems(final int startPosition, final int size) {
        isLoading = true;

        VkRepository.getFriends(startPosition, size, new ResultCallback<List<User>>() {
            @Override
            public void onResult(final List<User> resultList) {
                if (resultList != null) {
                    if (resultList.size() < size) {
                        isLoadComplete = true;
                    }

                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
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

