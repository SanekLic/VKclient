package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Rect;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import static com.my.vkclient.Constants.EDUCATION_FORMAT;
import static com.my.vkclient.Constants.FRIENDS_COMMON_COUNT_FORMAT;
import static com.my.vkclient.Constants.FRIENDS_COUNT_FORMAT;
import static com.my.vkclient.Constants.IntentKey.USER_ID_INTENT_KEY;

public class UserActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView userPhotoImageView;
    private TextView lastSeenTextView;
    private ImageView userStatusImageView;
    private TextView userStatusTextView;
    private ImageView userFriendsImageView;
    private TextView userFriendsTextView;
    private ImageView userFollowersImageView;
    private TextView userFollowersTextView;
    private ImageView userCityImageView;
    private TextView userCityTextView;
    private ImageView userEducationImageView;
    private TextView userEducationTextView;

    public static void show(final Context context, final int userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(Constants.IntentKey.USER_ID_INTENT_KEY, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        setupView();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        int userId = intent.getIntExtra(USER_ID_INTENT_KEY, 0);

        VkRepository.getInstance().getUser(userId, new ResultCallback<User>() {
            @Override
            public void onResult(final User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            userNameTextView.setText(String.format(Constants.NAME_FORMAT, user.getFirstName(), user.getLastName()));

                            lastSeenTextView.setText(Utils.getInstance().getSimpleDate(user.getLastSeen().getTime()));

                            setInfoAndVisibilityField(userStatusTextView, userStatusImageView, user.getStatus());

                            String friendsCountText = user.getCounters() != null ? String.format(FRIENDS_COUNT_FORMAT, user.getCounters().getFriends(), user.getCommonFriendsCount()) :
                                    String.format(FRIENDS_COMMON_COUNT_FORMAT, user.getCommonFriendsCount());
                            setInfoAndVisibilityField(userFriendsTextView, userFriendsImageView, friendsCountText);
                            setInfoAndVisibilityField(userFollowersTextView, userFollowersImageView, String.valueOf(user.getFollowersCount()));
                            setInfoAndVisibilityField(userCityTextView, userCityImageView, user.getHomeTown());

                            if (user.getFacultyName() != null) {
                                setInfoAndVisibilityField(userEducationTextView, userEducationImageView, String.format(EDUCATION_FORMAT, user.getUniversityName(), user.getFacultyName()));
                            } else {
                                setInfoAndVisibilityField(userEducationTextView, userEducationImageView, user.getUniversityName());
                            }

                            if (user.getCropPhoto() != null) {
                                userPhotoImageView.setTag(R.id.IMAGE_TAG_CROP, new Rect(user.getCropPhoto().getCropRectX(), user.getCropPhoto().getCropRectY(),
                                        user.getCropPhoto().getCropRectX2(), user.getCropPhoto().getCropRectY2()));

                                ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, user.getCropPhoto().getCropPhotoUrl(),
                                        user.getCropPhoto().getCropPhotoWidth(), user.getCropPhoto().getCropPhotoHeight());
                            } else {
                                ImageLoader.getInstance().getImageFromUrl(userPhotoImageView, user.getPhotoMaxUrl(), 0, 0);
                            }
                        }
                    }
                });
            }
        });
    }

    private void setupView() {
        userNameTextView = findViewById(R.id.userNameTextView);
        userPhotoImageView = findViewById(R.id.userPhotoImageView);
        lastSeenTextView = findViewById(R.id.lastSeenTextView);
        userStatusImageView = findViewById(R.id.userStatusImageView);
        userStatusTextView = findViewById(R.id.userStatusTextView);
        userFriendsImageView = findViewById(R.id.userFriendsImageView);
        userFriendsTextView = findViewById(R.id.userFriendsTextView);
        userFollowersImageView = findViewById(R.id.userFollowersImageView);
        userFollowersTextView = findViewById(R.id.userFollowersTextView);
        userCityImageView = findViewById(R.id.userCityImageView);
        userCityTextView = findViewById(R.id.userCityTextView);
        userEducationImageView = findViewById(R.id.userEducationImageView);
        userEducationTextView = findViewById(R.id.userEducationTextView);
    }


    private void setInfoAndVisibilityField(TextView textView, ImageView imageView, String text) {
        if (text == null || text.isEmpty()) {
            if (textView.getVisibility() != View.GONE) {
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }
        } else {
            textView.setText(text);

            if (textView.getVisibility() != View.VISIBLE) {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }
}
