package com.my.vkclient.gson;

import com.google.gson.Gson;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Doc;
import com.my.vkclient.entities.FriendResponse;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.GroupResponse;
import com.my.vkclient.entities.Link;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserResponse;
import com.my.vkclient.entities.Video;
import com.my.vkclient.entities.VkDate;
import com.my.vkclient.gson.typeadapter.BooleanGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.CropPhotoGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.DocGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.LinkGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.PhotoGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.PodcastGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.VideoGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.VkDateGsonTypeAdapter;

import java.util.List;

public class GsonAdapter {
    private static GsonAdapter instance;
    private Gson gson;
    private Gson gsonWithPhotoRypeAdapter;

    private GsonAdapter() {
        gson = new Gson().newBuilder()
                .registerTypeAdapter(VkDate.class, new VkDateGsonTypeAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanGsonTypeAdapter())
                .registerTypeAdapter(CropPhoto.class, new CropPhotoGsonTypeAdapter())
                .registerTypeAdapter(Photo.class, new PhotoGsonTypeAdapter())
                .registerTypeAdapter(Video.class, new VideoGsonTypeAdapter())
                .registerTypeAdapter(Doc.class, new DocGsonTypeAdapter())
                .registerTypeAdapter(Link.class, new LinkGsonTypeAdapter())
                .registerTypeAdapter(Podcast.class, new PodcastGsonTypeAdapter())
                .create();

        gsonWithPhotoRypeAdapter = new Gson().newBuilder()
                .registerTypeAdapter(Photo.class, new PhotoGsonTypeAdapter())
                .create();
    }

    public static GsonAdapter getInstance() {
        if (instance == null) {
            instance = new GsonAdapter();
        }

        return instance;
    }

    public Gson getGsonWithPhotoRypeAdapter() {
        return gsonWithPhotoRypeAdapter;
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

        if (userResponse == null || userResponse.getUserList() == null || userResponse.getUserList().size() == 0) {
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