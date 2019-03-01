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

    private List<UserInFriends> friendsList = new ArrayList<>();

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friendsList.get(position), null);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull List listPayload) {
        if (listPayload.size() != 0) {
            holder.bind(friendsList.get(position), (ArrayList<UserInFriends.FriendDifferences>) listPayload.get(0));
        } else {
            holder.bind(friendsList.get(position), null);
        }
    }

    @Override
    public void onViewRecycled(@NonNull FriendViewHolder holder) {
        super.onViewRecycled(holder);

        holder.recycled();
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void setItems(List<UserInFriends> friendsList) {
        List<UserInFriends> prev_friends = this.friendsList;
        this.friendsList = friendsList;

        for (int i = 0; i < this.friendsList.size(); i++) {
            if (i >= prev_friends.size()) {
                notifyItemRangeInserted(i, this.friendsList.size() - prev_friends.size());

                break;
            }

            ArrayList friend_differences = this.friendsList.get(i).compare(prev_friends.get(i));

            if (friend_differences.size() != 0) {
                notifyItemChanged(i, friend_differences);
            }
        }

        if (prev_friends.size() > this.friendsList.size()) {
            notifyItemRangeRemoved(this.friendsList.size(), prev_friends.size() - this.friendsList.size());
        }
    }

    public void clearItems() {
        this.friendsList.clear();
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private static final String friendStatusOfflineString = "(offline)";
        private static final String friendStatusOnlineString = "(online)";

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

        public void bind(UserInFriends friend, ArrayList<UserInFriends.FriendDifferences> differences) {
            if (differences == null
                    || differences.contains(UserInFriends.FriendDifferences.DIFFERENT_FIRST_NAME)
                    || differences.contains(UserInFriends.FriendDifferences.DIFFERENT_LAST_NAME)) {
                this.friendNameView.setText(new StringBuilder()
                        .append(friend.getFirst_name())
                        .append(" ")
                        .append(friend.getLast_name()).toString());
            }

            if (differences == null || differences.contains(UserInFriends.FriendDifferences.DIFFERENT_PHOTO_200_ORIG)) {
                this.friendPhotoView.setAlpha(0f);
                this.loadImageToImageViewAsync = new LoadImageToImageViewAsync(this.friendPhotoView);
                this.loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, friend.getPhoto_200_orig());
            }

            if (differences == null || differences.contains(UserInFriends.FriendDifferences.DIFFERENT_ONLINE)) {
                if (friend.getOnline() == 0) {
                    this.onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                    this.onlineStatusTextView.setText(friendStatusOfflineString);
                } else {
                    this.onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                    this.onlineStatusTextView.setText(friendStatusOnlineString);
                }
            }
        }

        public void recycled() {
            this.loadImageToImageViewAsync.cancel(false);
        }
    }
}

