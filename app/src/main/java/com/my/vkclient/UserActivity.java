package com.my.vkclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.entities.Size;
import com.my.vkclient.entities.User;

public class UserActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView userPhotoImageView;
    private LoadImageToImageViewAsync loadImageToImageViewAsync;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        userNameTextView = findViewById(R.id.UserNameTextView);
        userPhotoImageView = findViewById(R.id.UserPhotoImageView);

        VkRepository.getUser(intent.getIntExtra(MainActivity.USER_ID_KEY, 0), new ResultCallback<User>() {
            @Override
            public void onResult(final User user) {
                userNameTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        userNameTextView.setText(new StringBuilder()
                                .append(user.getFirst_name())
                                .append(" ")
                                .append(user.getLast_name()).toString());
                    }
                });

                userPhotoImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        userPhotoImageView.setImageDrawable(null);
                        loadImageToImageViewAsync = new LoadImageToImageViewAsync(userPhotoImageView);
                        String urlCropPhoto = user.getPhoto_max_orig();
                        if (user.getCrop_photo() != null) {
                            int maxWidth = 0;
                            for (Size size : user.getCrop_photo().getPhoto().getSizes()) {
                                if (size.getWidth() > maxWidth) {
                                    maxWidth = size.getWidth();
                                    urlCropPhoto = size.getUrl();
                                }
                            }
                            loadImageToImageViewAsync.setCrop(user.getCrop_photo().getRect());
                        }

                        loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlCropPhoto);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loadImageToImageViewAsync != null) {
            loadImageToImageViewAsync.cancel(false);
        }
    }
}
