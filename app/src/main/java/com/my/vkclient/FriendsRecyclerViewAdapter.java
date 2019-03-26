package com.my.vkclient;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendViewHolder> {

    private List<Friend> friendList = new ArrayList<>();

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);

        return new FriendViewHolder(view);
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

    public void setItems(List<Friend> friendList) {
        List<Friend> previousFriendList = this.friendList;
        this.friendList = friendList;

        for (int i = 0; i < this.friendList.size(); i++) {
            if (i >= previousFriendList.size()) {
                notifyItemRangeInserted(i, this.friendList.size() - previousFriendList.size());

                break;
            }

            ArrayList friend_differences = this.friendList.get(i).compare(previousFriendList.get(i));

            if (friend_differences.size() != 0) {
                notifyItemChanged(i, friend_differences);
            }
        }

        if (previousFriendList.size() > this.friendList.size()) {
            notifyItemRangeRemoved(this.friendList.size(), previousFriendList.size() - this.friendList.size());
        }
    }

    public void clearItems() {
        this.friendList.clear();
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private static final String FRIEND_STATUS_OFFLINE_STRING = "(offline)";
        private static final String FRIEND_STATUS_ONLINE_STRING = "(online)";

        private ImageView friendPhotoView;
        private TextView friendNameView;
        private TextView onlineStatusTextView;
        private ImageView onlineStatusImageView;
        private LoadImageToImageViewAsync loadImageToImageViewAsync;

        public FriendViewHolder(View itemView) {
            super(itemView);

            this.friendPhotoView = itemView.findViewById(R.id.friendPhotoView);
            this.friendNameView = itemView.findViewById(R.id.friendNameView);
            this.onlineStatusTextView = itemView.findViewById(R.id.onlineStatusTextView);
            this.onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
        }

        public void bind(Friend friend, ArrayList<Friend.FriendDifferences> differences) {
            if (differences == null
                    || differences.contains(Friend.FriendDifferences.DIFFERENT_FIRST_NAME)
                    || differences.contains(Friend.FriendDifferences.DIFFERENT_LAST_NAME)) {
                this.friendNameView.setText(new StringBuilder()
                        .append(friend.getFirst_name())
                        .append(" ")
                        .append(friend.getLast_name()).toString());
            }

            if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_PHOTO_200_ORIG)) {
                this.friendPhotoView.setAlpha(0f);
                this.loadImageToImageViewAsync = new LoadImageToImageViewAsync(this.friendPhotoView,true);
                this.loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, friend.getPhoto_200_orig());
            }

            if (differences == null || differences.contains(Friend.FriendDifferences.DIFFERENT_ONLINE)) {
                if (friend.getOnline() == 0) {
                    this.onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                    this.onlineStatusTextView.setText(FRIEND_STATUS_OFFLINE_STRING);
                } else {
                    this.onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                    this.onlineStatusTextView.setText(FRIEND_STATUS_ONLINE_STRING);
                }
            }
        }

        public void recycled() {
            this.loadImageToImageViewAsync.cancel(false);
            this.friendPhotoView.setAlpha(0f);
        }
    }
}

