package com.my.vkclient;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.FriendViewHolder> {

    private List<UserInFriends> friends = new ArrayList<>();

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bind(friends.get(position));
    }

    @Override
    public void onViewRecycled(FriendViewHolder holder) {
        super.onViewRecycled(holder);
        holder.recycled();
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(UserInFriends friend) {
            this.friendNameView.setText(new StringBuilder().append(friend.getFirst_name()).append(" ").append(friend.getLast_name()).toString());
            this.friendPhotoView.setAlpha(0f);
            this.loadImageToImageViewAsync = new LoadImageToImageViewAsync(this.friendPhotoView);
            this.loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, friend.getPhoto_200_orig());
            if (friend.getOnline() == 0) {
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                this.onlineStatusTextView.setText("(offline)");
            } else {
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                this.onlineStatusTextView.setText("(online)");
            }
        }

        public void recycled(){
            this.loadImageToImageViewAsync.cancel(false);
        }
    }

    public void setItems(List<UserInFriends> friends) {
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.friends.clear();
        notifyDataSetChanged();
    }
}

