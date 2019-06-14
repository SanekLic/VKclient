package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Rect;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.adapters.UserPhotoRecyclerViewAdapter;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.util.List;

import static com.my.vkclient.Constants.IntentKey.USER_ID_INTENT_KEY;
import static com.my.vkclient.Constants.STRING_EMPTY;
import static com.my.vkclient.Constants.UserActivity.COUNT_PHOTO_FORMAT;
import static com.my.vkclient.Constants.UserActivity.EDUCATION_FORMAT;
import static com.my.vkclient.Constants.UserActivity.FOLLOWERS_FORMAT;
import static com.my.vkclient.Constants.UserActivity.FRIENDS_COMMON_COUNT_FORMAT;
import static com.my.vkclient.Constants.UserActivity.FRIENDS_COUNT_FORMAT;
import static com.my.vkclient.Constants.UserActivity.ONLINE_FORMAT;
import static com.my.vkclient.Constants.UserActivity.STATE_ONLINE;

public class UserActivity extends AppCompatActivity {

    UserPhotoRecyclerViewAdapter userPhotoRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
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
    private String photoUrl;
    private TextView countPhotoTextView;
    private RecyclerView userPhotoRecyclerView;
    private int userId;

    public static void show(final Context context, final int userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(Constants.IntentKey.USER_ID_INTENT_KEY, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        userId = intent.getIntExtra(USER_ID_INTENT_KEY, 0);

        setupView();
        getData();
        setupPhotoRecyclerView();
    }

    private void setupPhotoRecyclerView() {
        userPhotoRecyclerView = findViewById(R.id.userPhotoRecyclerView);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        userPhotoRecyclerView.setLayoutManager(linearLayoutManager);
        userPhotoRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = Constants.RecyclerView.VIEW_MARGIN;
                }
            }
        });

        userPhotoRecyclerViewAdapter = new UserPhotoRecyclerViewAdapter(this, linearLayoutManager) {
            @Override
            public void load(int startPosition, int size, ResultCallback<List<Photo>> listResultCallback) {
                VkRepository.getInstance().getUserPhotos(userId, startPosition, size, listResultCallback);
            }

            @Override
            public void onLoadStateChanged(boolean state) {
            }
        };
        userPhotoRecyclerViewAdapter.setOnItemClickListener(new ResultCallback<Photo>() {
            @Override
            public void onResult(Photo photo) {
                ImageActivity.show(UserActivity.this, photo.getPhotoUrl());
            }
        });

        userPhotoRecyclerView.setAdapter(userPhotoRecyclerViewAdapter);

        userPhotoRecyclerViewAdapter.initialLoadItems();
    }

    private void setupView() {
        userNameTextView = findViewById(R.id.userNameTextView);
        userPhotoImageView = findViewById(R.id.userPhotoImageView);
        userPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUrl != null) {
                    ImageActivity.show(UserActivity.this, photoUrl);
                }
            }
        });
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
        countPhotoTextView = findViewById(R.id.countPhotoTextView);
    }

    private void getData() {
        VkRepository.getInstance().getUser(userId, new ResultCallback<User>() {
            @Override
            public void onResult(final User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            userNameTextView.setText(String.format(Constants.NAME_FORMAT, user.getFirstName(), user.getLastName()));

                            setLastSeen(user);
                            setInfoAndVisibilityField(userStatusTextView, userStatusImageView, user.getStatus());
                            setFriendsCount(user);
                            setFollowers(user);
                            setInfoAndVisibilityField(userCityTextView, userCityImageView, user.getHomeTown());
                            setEducation(user);

                            if (user.getCounters() != null && user.getCounters().getPhotos() > 0) {
                                countPhotoTextView.setText(String.format(COUNT_PHOTO_FORMAT, user.getCounters().getPhotos()));

                                if (countPhotoTextView.getVisibility() != View.VISIBLE) {
                                    countPhotoTextView.setVisibility(View.VISIBLE);
                                    userPhotoRecyclerView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (countPhotoTextView.getVisibility() != View.GONE) {
                                    countPhotoTextView.setVisibility(View.GONE);
                                    userPhotoRecyclerView.setVisibility(View.GONE);
                                }
                            }

                            if (user.getCropPhoto() != null) {
                                photoUrl = user.getCropPhoto().getCropPhotoUrl();

                                userPhotoImageView.setTag(R.id.IMAGE_TAG_CROP,
                                        new Rect(user.getCropPhoto().getCropRectX(), user.getCropPhoto().getCropRectY(),
                                                user.getCropPhoto().getCropRectX2(), user.getCropPhoto().getCropRectY2()));

                                ImageLoader.getInstance().getImageFromUrl(userPhotoImageView,
                                        user.getCropPhoto().getCropPhotoUrl(),
                                        user.getCropPhoto().getCropPhotoWidth(),
                                        user.getCropPhoto().getCropPhotoHeight());
                            } else {
                                photoUrl = user.getPhotoMaxUrl();

                                ImageLoader.getInstance().getImageFromUrl(userPhotoImageView,
                                        user.getPhotoMaxUrl(), 0, 0);
                            }
                        }
                    }
                });
            }
        });
    }

    private void setFriendsCount(User user) {
        String friendsCountText = STRING_EMPTY;

        if (user.getCounters() != null && user.getCounters().getFriends() != 0) {
            friendsCountText = String.format(FRIENDS_COUNT_FORMAT, user.getCounters().getFriends(), user.getCommonFriendsCount());
        } else if (user.getCommonFriendsCount() != 0) {
            friendsCountText = String.format(FRIENDS_COMMON_COUNT_FORMAT, user.getCommonFriendsCount());
        }

        setInfoAndVisibilityField(userFriendsTextView, userFriendsImageView, friendsCountText);
    }

    private void setFollowers(User user) {
        if (user.getFollowersCount() != 0) {
            setInfoAndVisibilityField(userFollowersTextView, userFollowersImageView,
                    String.format(FOLLOWERS_FORMAT, user.getFollowersCount()));

            if (userFollowersTextView.getVisibility() != View.VISIBLE) {
                userFollowersTextView.setVisibility(View.VISIBLE);
                userFollowersImageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (userFollowersTextView.getVisibility() != View.GONE) {
                userFollowersTextView.setVisibility(View.GONE);
                userFollowersImageView.setVisibility(View.GONE);
            }
        }
    }

    private void setLastSeen(User user) {
        if (user.getLastSeen() != null || user.getOnline()) {
            String onlineState = user.getOnline() ? STATE_ONLINE : String.format(ONLINE_FORMAT, Utils.getInstance().getSimpleDate(user.getLastSeen().getTime()));
            lastSeenTextView.setText(onlineState);

            if (lastSeenTextView.getVisibility() != View.VISIBLE) {
                lastSeenTextView.setVisibility(View.VISIBLE);
            }
        } else {
            if (lastSeenTextView.getVisibility() != View.GONE) {
                lastSeenTextView.setVisibility(View.GONE);
            }
        }
    }

    private void setEducation(User user) {
        if (user.getFacultyName() != null && !user.getFacultyName().isEmpty() &&
                user.getUniversityName() != null && !user.getUniversityName().isEmpty()) {

            setInfoAndVisibilityField(userEducationTextView, userEducationImageView,
                    String.format(EDUCATION_FORMAT, user.getUniversityName(), user.getFacultyName()));

        } else if (user.getFacultyName() != null && !user.getFacultyName().isEmpty()) {
            setInfoAndVisibilityField(userEducationTextView, userEducationImageView, user.getFacultyName());
        } else {
            setInfoAndVisibilityField(userEducationTextView, userEducationImageView, user.getUniversityName());
        }
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
