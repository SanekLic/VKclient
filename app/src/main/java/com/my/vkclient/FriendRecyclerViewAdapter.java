package com.my.vkclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.FriendViewHolder> {

    private List<UserInFriends> friendsListViewAdapter = new ArrayList<>();

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bind(friendsListViewAdapter.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsListViewAdapter.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView friendPhotoView;
        private TextView friendNameView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            friendPhotoView = itemView.findViewById(R.id.friendPhotoView);
            friendNameView = itemView.findViewById(R.id.friendNameView);
        }

        public void bind(UserInFriends friend) {
            friendNameView.setText(new StringBuilder().append(friend.getFirst_name()).append(" ").append(friend.getLast_name()).toString());
            friendPhotoView.setVisibility(View.INVISIBLE);
            new DownloadImageTask(friendPhotoView).execute(friend.getPhoto_200_orig());
        }
    }

    public void setItems(List<UserInFriends> friends) {
        friendsListViewAdapter.addAll(friends);
        notifyDataSetChanged();
    }

    public void clearItems() {
        friendsListViewAdapter.clear();
        notifyDataSetChanged();
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        bmImage.setVisibility(View.VISIBLE);
    }
}