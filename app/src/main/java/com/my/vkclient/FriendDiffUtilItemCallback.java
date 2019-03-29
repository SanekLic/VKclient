package com.my.vkclient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

public class FriendDiffUtilItemCallback extends DiffUtil.ItemCallback<Friend> {
    @Override
    public boolean areItemsTheSame(@NonNull Friend oldFriend, @NonNull Friend newFriend) {
        return oldFriend.getId() == newFriend.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Friend oldFriend, @NonNull Friend newFriend) {
        return oldFriend.equals(newFriend);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull Friend oldFriend, @NonNull Friend newFriend) {
        return oldFriend.compare(newFriend);
    }
}
