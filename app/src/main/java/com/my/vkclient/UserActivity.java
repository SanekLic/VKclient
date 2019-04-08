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
        User user = (User) intent.getSerializableExtra(MainActivity.USER_INTENT_KEY);

        userNameTextView = findViewById(R.id.userNameTextView);
        userPhotoImageView = findViewById(R.id.userPhotoImageView);

        userNameTextView.setText(new StringBuilder()
                .append(user.getFirstName())
                .append(" ")
                .append(user.getLastName()).toString());
        loadImageToImageViewAsync = new LoadImageToImageViewAsync(userPhotoImageView);
        String urlCropPhoto = user.getPhotoMaxOrig();
        if (user.getCropPhoto() != null) {
            int maxWidth = 0;
            for (Size size : user.getCropPhoto().getPhoto().getSizes()) {
                if (size.getWidth() > maxWidth) {
                    maxWidth = size.getWidth();
                    urlCropPhoto = size.getUrl();
                }
            }
            loadImageToImageViewAsync.setCrop(user.getCropPhoto().getRect());
        }
        loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlCropPhoto);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loadImageToImageViewAsync != null) {
            loadImageToImageViewAsync.cancel(false);
        }
    }
}
