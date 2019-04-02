package com.my.vkclient;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.my.vkclient.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDiffUtilCallback extends DiffUtil.Callback {
    private List<User> oldUserList;
    private List<User> newUserList;

    public UserDiffUtilCallback() {
        oldUserList = new ArrayList<>();
        newUserList = new ArrayList<>();
    }

    @Override
    public int getOldListSize() {
        return oldUserList.size();
    }

    @Override
    public int getNewListSize() {
        return newUserList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUserList.get(oldItemPosition).getId() == newUserList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUserList.get(oldItemPosition).equals(newUserList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return oldUserList.get(oldItemPosition).compare(newUserList.get(newItemPosition));
    }

    public UserDiffUtilCallback setOldUserList(List<User> oldUserList) {
        this.oldUserList.clear();
        this.oldUserList.addAll(oldUserList);

        return this;
    }

    public UserDiffUtilCallback setNewUserList(List<User> newUserList) {
        this.newUserList.clear();
        this.newUserList.addAll(newUserList);

        return this;
    }
}