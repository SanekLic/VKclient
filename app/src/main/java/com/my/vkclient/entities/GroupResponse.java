package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {
    @SerializedName("response")
    private List<Group> groupList;

    public List<Group> getGroupList() {
        return groupList;
    }
}
