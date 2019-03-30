package com.my.vkclient;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private static final int PAGE_SIZE = 10;
    private List<Friend> friendList = new ArrayList<>();
    private RecyclerView friendRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLoadComplete;

    FriendRecyclerViewAdapter(RecyclerView friendRecyclerView) {
        this.friendRecyclerView = friendRecyclerView;
        linearLayoutManager = new LinearLayoutManager(friendRecyclerView.getContext());
        friendRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FriendViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        validateLoadMoreItems();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friendList.get(position), null);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull List listPayload) {
        if (listPayload.size() != 0) {
            holder.bind(friendList.get(position), (ArrayList<Friend.FriendDifferences>) listPayload.get(0));
        } else {
            holder.bind(friendList.get(position), null);
        }
    }

    @Override
    public void onViewRecycled(@NonNull FriendViewHolder holder) {
        super.onViewRecycled(holder);

        holder.recycled();
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void initialLoadItems() {
        loadMoreItems(0, PAGE_SIZE * 2);
    }

    public void setItems(List<Friend> friendList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FriendDiffUtilCallback(this.friendList, friendList));
        this.friendList.clear();
        this.friendList.addAll(friendList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void addItems(List<Friend> friendList) {
        this.friendList.addAll(friendList);
        notifyItemRangeInserted(this.friendList.size() - friendList.size(), friendList.size());
    }

    public void refreshItems() {
        Log.d("dfd", "refreshItems: ");
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

        VkRepository.getFriends(startPosition, size, new ResultCallback<List<Friend>>() {
            @Override
            public void onResult(final List<Friend> resultList) {
                if (resultList != null) {
                    if (resultList.size() < size) {
                        isLoadComplete = true;
                    }

                    friendRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            addItems(resultList);
                            isLoading = false;
                        }
                    });
                } else {
                    isLoadComplete = true;
                    isLoading = false;
                }
            }
        });
    }
}

