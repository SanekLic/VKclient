package com.my.vkclient;

import com.google.gson.Gson;
import com.my.vkclient.entities.FriendResponse;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.GroupResponse;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static List<User> importFriendsFromJson(String jsonFriends) {
        FriendResponse friendResponse = new Gson().fromJson(jsonFriends, FriendResponse.class);

        if (friendResponse == null) {
            return null;
        }

        return friendResponse.getResponse().getUserList();
    }

    public static User importUserFromJson(String jsonUser) {
        UserResponse userResponse = new Gson().fromJson(jsonUser, UserResponse.class);

        if (userResponse == null) {
            return null;
        }

        return userResponse.getUserList().get(0);
    }

    public static NewsResponse.Response importNewsFromJson(String jsonNews) {
        NewsResponse newsResponse = new Gson().fromJson(jsonNews, NewsResponse.class);

        if (newsResponse == null) {
            return null;
        }

        return newsResponse.getResponse();
    }

    public static Group importGroupFromJson(String jsonGroup) {
        GroupResponse groupResponse = new Gson().fromJson(jsonGroup, GroupResponse.class);

        if (groupResponse == null) {
            return null;
        }

        return groupResponse.getGroupList().get(0);
    }

}
