package com.my.vkclient.utils;

import com.google.gson.Gson;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.FriendResponse;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.GroupResponse;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserResponse;
import com.my.vkclient.entities.Video;
import com.my.vkclient.entities.VkDate;

import java.util.List;

public class GsonAdapter {
    private static GsonAdapter instance;
    private Gson gson;

    private GsonAdapter() {
        gson = new Gson().newBuilder()
                .registerTypeAdapter(VkDate.class, new VkDateGsonTypeAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanGsonTypeAdapter())
                .registerTypeAdapter(CropPhoto.class, new CropPhotoGsonTypeAdapter())
                .registerTypeAdapter(Photo.class, new PhotoGsonTypeAdapter())
                .registerTypeAdapter(Video.class, new VideoGsonTypeAdapter())
                .create();
    }

    public static GsonAdapter getInstance() {
        if (instance == null) {
            instance = new GsonAdapter();
        }

        return instance;
    }

    public List<User> getFriendsFromJson(String jsonFriends) {
        FriendResponse friendResponse = gson.fromJson(jsonFriends, FriendResponse.class);

        if (friendResponse == null || friendResponse.getResponse() == null) {
            return null;
        }

        return friendResponse.getResponse().getUserList();
    }

    public User getUserFromJson(String jsonUser) {
        UserResponse userResponse = gson.fromJson(jsonUser, UserResponse.class);

        if (userResponse == null || userResponse.getUserList() == null) {
            return null;
        }

        return userResponse.getUserList().get(0);
    }

    public Group getGroupFromJson(String jsonGroup) {
        GroupResponse groupResponse = gson.fromJson(jsonGroup, GroupResponse.class);

        if (groupResponse == null || groupResponse.getGroupList() == null) {
            return null;
        }

        return groupResponse.getGroupList().get(0);
    }

    public NewsResponse.Response getNewsFromJson(String jsonNews) {
        NewsResponse newsResponse = gson.fromJson(jsonNews, NewsResponse.class);

        if (newsResponse == null) {
            return null;
        }

        return newsResponse.getResponse();
    }
}