package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.my.vkclient.R;
import com.my.vkclient.utils.ImageLoader;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIM;
import static com.my.vkclient.Constants.IntentKey.IMAGE_URL_INTENT_KEY;

public class ImageActivity extends AppCompatActivity {

    public static void show(final Context context, final String imageUrl) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(IMAGE_URL_INTENT_KEY, imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image);

        String imageUrl = getIntent().getStringExtra(IMAGE_URL_INTENT_KEY);

        setupImageView(imageUrl);
    }

    private void setupImageView(String imageUrl) {
        ImageView imageView = findViewById(R.id.imageView);
        ImageLoader.getInstance().getImageFromUrl(imageView, imageUrl, 0, 0, DEFAULT_ANIM);
    }
}
