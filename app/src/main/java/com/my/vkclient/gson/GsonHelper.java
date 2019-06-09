package com.my.vkclient.gson;

import com.google.gson.Gson;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Doc;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.Link;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserResponse;
import com.my.vkclient.entities.Video;
import com.my.vkclient.entities.response.FriendResponse;
import com.my.vkclient.entities.response.GroupResponse;
import com.my.vkclient.entities.response.LikesResponse;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.gson.typeadapter.BooleanGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.CropPhotoGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.DocGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.LinkGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.PhotoGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.PodcastGsonTypeAdapter;
import com.my.vkclient.gson.typeadapter.VideoGsonTypeAdapter;

import java.util.List;

public class GsonHelper {
    private static GsonHelper instance;
    private Gson gson;
    private Gson gsonWithPhotoTypeAdapter;

    private GsonHelper() {
        gson = new Gson().newBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanGsonTypeAdapter())
                .registerTypeAdapter(CropPhoto.class, new CropPhotoGsonTypeAdapter())
                .registerTypeAdapter(Photo.class, new PhotoGsonTypeAdapter())
                .registerTypeAdapter(Video.class, new VideoGsonTypeAdapter())
                .registerTypeAdapter(Doc.class, new DocGsonTypeAdapter())
                .registerTypeAdapter(Link.class, new LinkGsonTypeAdapter())
                .registerTypeAdapter(Podcast.class, new PodcastGsonTypeAdapter())
                .create();

        gsonWithPhotoTypeAdapter = new Gson().newBuilder()
                .registerTypeAdapter(Photo.class, new PhotoGsonTypeAdapter())
                .create();
    }

    public static GsonHelper getInstance() {
        if (instance == null) {
            instance = new GsonHelper();
        }

        return instance;
    }

    public Gson getGsonWithPhotoTypeAdapter() {
        return gsonWithPhotoTypeAdapter;
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

        if (newsResponse.getResponse().getNewsList() != null) {
            for (News news : newsResponse.getResponse().getNewsList()) {
                setOwnerToNews(newsResponse, news);

                if (news.getCopyHistory() != null) {
                    setOwnerToNews(newsResponse, news.getCopyHistory().get(0));
                }
            }
        }

        return newsResponse.getResponse();
    }

    public LikesResponse.Response getLikesResponseFromJson(String jsonNews) {
        LikesResponse likesResponse = gson.fromJson(jsonNews, LikesResponse.class);

        if (likesResponse == null || likesResponse.getResponse() == null) {
            return null;
        }

        return likesResponse.getResponse();
    }

    private void setOwnerToNews(NewsResponse newsResponse, News news) {
        int ownerId = news.getSourceId() == 0 ? news.getFromId() : news.getSourceId();
        if (ownerId > 0) {
            if (newsResponse.getResponse().getUserList() != null) {
                for (User user : newsResponse.getResponse().getUserList()) {
                    if (user.getId() == ownerId) {
                        news.setUser(user);

                        break;
                    }
                }
            }
        } else {
            ownerId *= -1;
            if (newsResponse.getResponse().getGroupList() != null) {
                for (Group group : newsResponse.getResponse().getGroupList()) {
                    if (group.getId() == ownerId) {
                        news.setGroup(group);

                        break;
                    }
                }
            }
        }
    }
}