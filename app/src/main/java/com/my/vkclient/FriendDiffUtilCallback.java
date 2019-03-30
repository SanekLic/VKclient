package com.my.vkclient;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public class FriendDiffUtilCallback extends DiffUtil.Callback {
    private List<Friend> oldFriendList;
    private List<Friend> newFriendList;

    FriendDiffUtilCallback(List<Friend> newFriendList, List<Friend> oldFriendList) {
        this.oldFriendList = oldFriendList;
        this.newFriendList = newFriendList;
    }

    @Override
    public int getOldListSize() {
        return oldFriendList.size();
    }

    @Override
    public int getNewListSize() {
        return newFriendList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFriendList.get(oldItemPosition).getId() == newFriendList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFriendList.get(oldItemPosition).equals(newFriendList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return oldFriendList.get(oldItemPosition).compare(newFriendList.get(newItemPosition));
    }
}
