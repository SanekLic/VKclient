package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.activity.UserActivity;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.SharedPreferencesHelper;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;
import static com.my.vkclient.Constants.NAME_FORMAT;

public class SettingsFragment extends Fragment {
    public final Handler mainLooperHandler;
    private View.OnClickListener onLogoutClickListener;
    private ImageView profilePhotoImageView;
    private TextView profileNameTextView;

    public SettingsFragment() {
        mainLooperHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        setupView(view);
        setData();
    }

    private void setupView(@NonNull final View view) {
        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLogoutClickListener != null) {
                    onLogoutClickListener.onClick(view);
                }
            }
        });

        Switch animationSwitch = view.findViewById(R.id.animationSwitch);
        animationSwitch.setChecked(SharedPreferencesHelper.getInstance().isAnimationEnable());
        animationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesHelper.getInstance().setAnimationEnable(isChecked);
            }
        });

        profilePhotoImageView = view.findViewById(R.id.profilePhotoImageView);
        profileNameTextView = view.findViewById(R.id.profileNameTextView);

        View frameProfileView = view.findViewById(R.id.frameProfileView);
        frameProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.show(SettingsFragment.this.getContext(), SharedPreferencesHelper.getInstance().getProfileId());
            }
        });
    }

    private void setData() {
        int profileId = SharedPreferencesHelper.getInstance().getProfileId();
        VkRepository.getInstance().getUser(profileId, new ResultCallback<User>() {
            @Override
            public void onResult(final User profile) {
                if (profile != null) {
                    SharedPreferencesHelper.getInstance().setProfileId(profile.getId());

                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            profileNameTextView.setText(String.format(NAME_FORMAT, profile.getFirstName(), profile.getLastName()));
                            profilePhotoImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                            ImageLoader.getInstance().getImageFromUrl(profilePhotoImageView, profile.getPhoto100Url(), 0, 0, DEFAULT_ANIMATION);

                            profilePhotoImageView.setVisibility(View.VISIBLE);
                            profileNameTextView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    public void setOnLogoutClickListener(View.OnClickListener onLogoutClickListener) {
        this.onLogoutClickListener = onLogoutClickListener;
    }
}
