package com.my.vkclient;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.my.vkclient.Entities.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendDiffUtilCallback extends DiffUtil.Callback {
    private List<Friend> oldFriendList;
    private List<Friend> newFriendList;

    public FriendDiffUtilCallback() {
        oldFriendList = new ArrayList<>();
        newFriendList = new ArrayList<>();
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

    public FriendDiffUtilCallback setOldFriendList(List<Friend> oldFriendList) {
        this.oldFriendList.clear();
        this.oldFriendList.addAll(oldFriendList);

        return this;
    }

    public FriendDiffUtilCallback setNewFriendList(List<Friend> newFriendList) {
        this.newFriendList.clear();
        this.newFriendList.addAll(newFriendList);

        return this;
    }
}