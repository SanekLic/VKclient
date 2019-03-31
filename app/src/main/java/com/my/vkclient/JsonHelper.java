package com.my.vkclient;

import com.google.gson.Gson;
import com.my.vkclient.Entities.Friend;
import com.my.vkclient.Entities.FriendResponse;
import com.my.vkclient.Entities.User;
import com.my.vkclient.Entities.UserResponse;

import java.util.List;

public class JsonHelper {

    public static List<Friend> importFriendsFromJson(String jsonFriends) {
        Gson gson = new Gson();
        FriendResponse friendResponse = gson.fromJson(jsonFriends, FriendResponse.class);

        return friendResponse.getResponse().getItems();
    }

    public static User importUserFromJson(String jsonUser) {
        Gson gson = new Gson();
        UserResponse userResponse = gson.fromJson(jsonUser, UserResponse.class);

        return userResponse.getResponse().get(0);
    }
}
