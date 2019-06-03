package com.my.vkclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.Repository.VkRepository;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Size;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

import static com.my.vkclient.Constants.STRING_SPACE;
import static com.my.vkclient.Constants.USER_ID_INTENT_KEY;

public class UserActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView userPhotoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        int userId = intent.getIntExtra(USER_ID_INTENT_KEY, 0);

        userNameTextView = findViewById(R.id.userNameTextView);
        userPhotoImageView = findViewById(R.id.userPhotoImageView);

        VkRepository.getUserById(userId, new ResultCallback<User>() {
            @Override
            public void onResult(final User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userNameTextView.setText(new StringBuilder()
                                .append(user.getFirstName())
                                .append(STRING_SPACE)
                                .append(user.getLastName()).toString());

                        if (user.getCropPhoto() != null) {
                            setMaxSizePhotoToImageView(user.getCropPhoto());
                        } else {
                            ImageLoader.getImageFromUrl(userPhotoImageView, user.getPhotoMaxUrl(), 0, 0);
                        }
                    }
                });
            }
        });
    }

    private void setMaxSizePhotoToImageView(CropPhoto cropPhoto) {
        Size showSize = cropPhoto.getPhoto().getSizes().get(0);

        for (Size size : cropPhoto.getPhoto().getSizes()) {
            if (size.getWidth() > showSize.getWidth()) {
                showSize = size;
            }
        }

        userPhotoImageView.setTag(R.id.IMAGE_TAG_CROP, cropPhoto.getRect());
        ImageLoader.getImageFromUrl(userPhotoImageView, showSize.getUrl(), showSize.getWidth(), showSize.getHeight());
    }
}
