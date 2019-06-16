package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Group;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;
import static com.my.vkclient.Constants.IntentKey.GROUP_ID_INTENT_KEY;

public class GroupInfoDialogFragment extends BaseDialogFragment {
    private ImageView groupPhotoImageView;
    private TextView groupNameTextView;
    private ImageView verifiedIconImageView;
    private TextView groupStatusTextView;
    private ImageView groupSiteIconImageView;
    private TextView groupSiteTextView;
    private ImageView groupDescriptionIconImageView;
    private TextView groupDescriptionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_group_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView(view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            int groupId = bundle.getInt(GROUP_ID_INTENT_KEY);
            VkRepository.getInstance().getGroup(groupId, new ResultCallback<Group>() {
                @Override
                public void onResult(Group result) {
                    if (result != null) {
                        setGroup(result);
                    }
                }
            });
        }
    }

    public void setGroup(Group group) {
        groupNameTextView.setText(group.getName());

        groupPhotoImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
        ImageLoader.getInstance().getImageFromUrl(groupPhotoImageView, group.getPhoto100Url(), 0, 0, DEFAULT_ANIMATION);

        if (group.getVerified() != null && group.getVerified()) {
            if (verifiedIconImageView.getVisibility() != View.VISIBLE) {
                verifiedIconImageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (verifiedIconImageView.getVisibility() != View.GONE) {
                verifiedIconImageView.setVisibility(View.GONE);
            }
        }

        groupStatusTextView.setText(group.getStatus());

        Utils.getInstance().setInfoAndVisibilityFieldView(groupSiteTextView, groupSiteIconImageView, group.getSite());
        Utils.getInstance().setInfoAndVisibilityFieldView(groupDescriptionTextView, groupDescriptionIconImageView, group.getDescription());

        restoreScrollPosition();
    }

    private void setupView(@NonNull View view) {
        groupPhotoImageView = view.findViewById(R.id.groupPhotoImageView);
        groupNameTextView = view.findViewById(R.id.groupNameTextView);
        verifiedIconImageView = view.findViewById(R.id.verifiedIconImageView);
        groupStatusTextView = view.findViewById(R.id.groupStatusTextView);
        groupSiteIconImageView = view.findViewById(R.id.groupSiteIconImageView);
        groupSiteTextView = view.findViewById(R.id.groupSiteTextView);
        groupDescriptionIconImageView = view.findViewById(R.id.groupDescriptionIconImageView);
        groupDescriptionTextView = view.findViewById(R.id.groupDescriptionTextView);
    }
}
