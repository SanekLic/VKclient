package com.my.vkclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Rect;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;

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

        VkRepository.getInstance().getUser(userId, new ResultCallback<User>() {
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
                            userPhotoImageView.setTag(R.id.IMAGE_TAG_CROP, new Rect(user.getCropPhoto().getCropRectX(), user.getCropPhoto().getCropRectY(),
                                    user.getCropPhoto().getCropRectX2(), user.getCropPhoto().getCropRectY2()));
                            ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, user.getCropPhoto().getCropPhotoUrl(),
                                    user.getCropPhoto().getCropPhotoWidth(), user.getCropPhoto().getCropPhotoHeight());
                        } else {
                            ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, user.getPhotoMaxUrl(), 0, 0);
                        }
                    }
                });
            }
        });
    }
}
