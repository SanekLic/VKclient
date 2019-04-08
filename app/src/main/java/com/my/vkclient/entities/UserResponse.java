package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("response")
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }
}
