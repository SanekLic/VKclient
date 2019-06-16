package com.my.vkclient.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.Group;
import com.my.vkclient.utils.ImageLoader;

import static com.my.vkclient.Constants.ImageLoader.DEFAULT_ANIMATION;

class GroupViewHolder extends BaseViewHolder<Group> {
    private ImageView groupPhotoImageView;
    private TextView groupNameTextView;
    private ImageView verifiedIconImageView;
    private TextView groupActivityTextView;

    GroupViewHolder(View itemView) {
        super(itemView);

        groupPhotoImageView = itemView.findViewById(R.id.groupPhotoImageView);
        groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
        verifiedIconImageView = itemView.findViewById(R.id.verifiedIconImageView);
        groupActivityTextView = itemView.findViewById(R.id.groupActivityTextView);
    }

    public void bind(Group group) {
        groupNameTextView.setText(group.getName());
        groupActivityTextView.setText(group.getActivity());

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
    }
}
