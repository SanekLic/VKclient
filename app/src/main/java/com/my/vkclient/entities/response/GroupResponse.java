package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;
import com.my.vkclient.entities.Group;

import java.util.List;

public class GroupResponse {
    @SerializedName("response")
    private List<Group> groupList;

    public List<Group> getGroupList() {
        return groupList;
    }
}
