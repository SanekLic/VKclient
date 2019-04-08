package com.my.vkclient;

import android.content.Intent;
import android.graphics.Bitmap;
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
//    private LoadImageToImageViewAsync loadImageToImageViewAsync;
    private ImageLoader imageLoader;

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
        userPhotoImageView.setImageDrawable(null);
//        loadImageToImageViewAsync = new LoadImageToImageViewAsync(userPhotoImageView);
        imageLoader = new ImageLoader(this);
        String urlCropPhoto = user.getPhotoMaxOrig();
        if (user.getCropPhoto() != null) {
            int maxWidth = 0;
            for (Size size : user.getCropPhoto().getPhoto().getSizes()) {
                if (size.getWidth() > maxWidth) {
                    maxWidth = size.getWidth();
                    urlCropPhoto = size.getUrl();
                }
            }
            imageLoader.setCrop(user.getCropPhoto().getRect());
//            loadImageToImageViewAsync.setCrop(user.getCropPhoto().getRect());
        }
        imageLoader.getImageFromUrl(urlCropPhoto, new ResultCallback<Bitmap>() {
            @Override
            public void onResult(final Bitmap resultBitmap) {
                userPhotoImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        userPhotoImageView.setImageBitmap(resultBitmap);
                    }
                });
            }
        });
//        loadImageToImageViewAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlCropPhoto);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (imageLoader != null) {
            imageLoader.cancel();
        }
//        if (loadImageToImageViewAsync != null) {
//            loadImageToImageViewAsync.cancel(false);
//        }
    }
}
