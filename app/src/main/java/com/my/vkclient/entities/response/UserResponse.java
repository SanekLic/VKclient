package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;
import com.my.vkclient.entities.User;

import java.util.List;

public class UserResponse {
    @SerializedName("response")
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }
}
