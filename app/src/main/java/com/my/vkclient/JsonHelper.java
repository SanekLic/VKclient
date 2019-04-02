package com.my.vkclient;

import com.google.gson.Gson;
import com.my.vkclient.entities.FriendResponse;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static List<User> importFriendsFromJson(String jsonFriends) {
        Gson gson = new Gson();
        FriendResponse friendResponse = gson.fromJson(jsonFriends, FriendResponse.class);

        if (friendResponse == null) {
            return new ArrayList<>();
        }

        return friendResponse.getResponse().getItems();
    }

    public static User importUserFromJson(String jsonUser) {
        Gson gson = new Gson();
        UserResponse userResponse = gson.fromJson(jsonUser, UserResponse.class);

        if (userResponse == null) {
            return null;
        }

        return userResponse.getResponse().get(0);
    }
}
