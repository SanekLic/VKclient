package com.my.vkclient;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.entities.User;

import java.util.ArrayList;

class FriendViewHolder extends RecyclerView.ViewHolder {
    private static final String FRIEND_STATUS_OFFLINE_STRING = "(offline)";
    private static final String FRIEND_STATUS_ONLINE_STRING = "(online)";
    private ImageView friendPhotoView;
    private TextView friendNameView;
    private TextView onlineStatusTextView;
    private ImageView onlineStatusImageView;
//    private LoadImageToImageViewAsync loadImageToImageViewAsync;
    private ImageLoader imageLoader;

    public FriendViewHolder(View itemView) {
        super(itemView);

        friendPhotoView = itemView.findViewById(R.id.friendPhotoImageView);
        friendNameView = itemView.findViewById(R.id.friendNameTextView);
        onlineStatusTextView = itemView.findViewById(R.id.onlineStatusTextView);
        onlineStatusImageView = itemView.findViewById(R.id.onlineStatusImageView);
    }

    public void bind(User user, ArrayList<User.UserDifferences> differences) {
        if (differences == null
                || differences.contains(User.UserDifferences.DIFFERENT_FIRST_NAME)
                || differences.contains(User.UserDifferences.DIFFERENT_LAST_NAME)) {
            friendNameView.setText(new StringBuilder()
                    .append(user.getFirstName())
                    .append(" ")
                    .append(user.getLastName()).toString());
        }

        if (differences == null || differences.contains(User.UserDifferences.DIFFERENT_PHOTO_100)) {
            friendPhotoView.setImageDrawable(null);
            friendPhotoView.setAlpha(0f);
            imageLoader = new ImageLoader(friendPhotoView.getContext()).setCircular(true);
            imageLoader.getImageFromUrl(user.getPhoto100(), new ResultCallback<Bitmap>() {
                @Override
                public void onResult(final Bitmap resultBitmap) {
                    friendPhotoView.post(new Runnable() {
                        @Override
                        public void run() {
                            friendPhotoView.setImageBitmap(resultBitmap);
                            Animator animator = AnimatorInflater.loadAnimator(friendPhotoView.getContext(), R.animator.image_change_visibility_animator);
                            animator.setTarget(friendPhotoView);
                            animator.start();
                        }
                    });
                }
            });
//            loadImageToImageViewAsync = new LoadImageToImageViewAsync(friendPhotoView).setCircular(true);
//            loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user.getPhoto100());
        }

        if (differences == null || differences.contains(User.UserDifferences.DIFFERENT_ONLINE)) {
            if (user.getOnline() == 0) {
                onlineStatusImageView.setImageResource(android.R.drawable.presence_offline);
                onlineStatusTextView.setText(FRIEND_STATUS_OFFLINE_STRING);
            } else {
                onlineStatusImageView.setImageResource(android.R.drawable.presence_online);
                onlineStatusTextView.setText(FRIEND_STATUS_ONLINE_STRING);
            }
        }
    }

    public void recycled() {
        imageLoader.cancel();
//        loadImageToImageViewAsync.cancel(false);
    }
}
