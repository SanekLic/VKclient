package com.my.vkclient;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private static final int PAGE_SIZE = 20;
    private static final long intervalMillis = 5000;
    private List<User> friendList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLoadComplete;
    private UserDiffUtilCallback userDiffUtilCallback;
    private boolean runAutoUpdate = false;
    private Thread friendListUpdateThread;
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private ResultCallback<User> itemClickListener;

    FriendRecyclerViewAdapter(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        userDiffUtilCallback = new UserDiffUtilCallback();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);

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
        holder.bind(friendList.get(position), null);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull List listPayload) {
        if (listPayload.size() != 0) {
            holder.bind(friendList.get(position), (ArrayList<User.UserDifferences>) listPayload.get(0));
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

    public void autoUpdateItems(boolean enable) {
        runAutoUpdate = enable;
        if (enable) {
            if (friendListUpdateThread == null) {
                friendListUpdateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (runAutoUpdate) {
                            try {
                                Thread.sleep(intervalMillis);
                                mainLooperHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadReplaceItems();
                                    }
                                });
                            } catch (InterruptedException e) {
                                Log.d("Thread", "InterruptedException");
                                e.printStackTrace();
                            }
                        }
                    }
                });
                friendListUpdateThread.setDaemon(true);
                friendListUpdateThread.start();
            }
        } else {
            if (friendListUpdateThread != null) {
                friendListUpdateThread = null;
            }
        }
    }

    public void setOnItemClickListener(ResultCallback<User> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setItems(List<User> newFriendList) {
        userDiffUtilCallback
                .setOldUserList(friendList)
                .setNewUserList(newFriendList);
        friendList.clear();
        friendList.addAll(newFriendList);
        DiffUtil.calculateDiff(userDiffUtilCallback).dispatchUpdatesTo(this);
    }

    private void addItems(List<User> addFriendList) {
        friendList.addAll(addFriendList);
        notifyItemRangeInserted(friendList.size() - addFriendList.size(), addFriendList.size());
    }

    private void replaceItems(List<User> replaceFriendList, int startPosition) {
        userDiffUtilCallback.setOldUserList(friendList);

        for (int i = 0; i < replaceFriendList.size(); i++) {
            if (friendList.size() <= startPosition) {
                break;
            }

            friendList.remove(startPosition);
        }

        friendList.addAll(startPosition, replaceFriendList);
        userDiffUtilCallback.setNewUserList(friendList);
        DiffUtil.calculateDiff(userDiffUtilCallback).dispatchUpdatesTo(this);
    }

    private void loadReplaceItems() {
        final int visibleItemCount = linearLayoutManager.getChildCount();
        if (visibleItemCount > 0) {
            final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            VkRepository.getFriends(firstVisibleItemPosition, visibleItemCount,
                    new ResultCallback<List<User>>() {
                        @Override
                        public void onResult(final List<User> resultList) {
                            mainLooperHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    replaceItems(resultList, firstVisibleItemPosition);
                                }
                            });
                        }
                    });
        }
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
                    isLoadComplete = true;
                    isLoading = false;
                }
            }
        });
    }
}

