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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
    public int getItemCount() {
        return friends.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView friendPhotoView;
        private TextView friendNameView;
        private TextView onlineStatusTextView;
        private ImageView onlineStatusImageView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            this.friendPhotoView = itemView.findViewById(R.id.friendPhotoView);
            this.friendNameView = itemView.findViewById(R.id.friendNameView);
            this.onlineStatusTextView = itemView.findViewById(R.id.onlineStatusTextView);
            this.onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
        }

        public void bind(UserInFriends friend) {
            this.friendNameView.setText(new StringBuilder().append(friend.getFirst_name()).append(" ").append(friend.getLast_name()).toString());
            this.friendPhotoView.setVisibility(View.INVISIBLE);
            new DownloadImageTask(this.friendPhotoView).execute(friend.getPhoto_200_orig());
            if(friend.getOnline()==0) {
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                this.onlineStatusTextView.setText("(offline)");
            }else{
                this.onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                this.onlineStatusTextView.setText("(online)");
            }
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