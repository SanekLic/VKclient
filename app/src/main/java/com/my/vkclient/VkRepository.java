package com.my.vkclient;

import android.util.Log;

import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.my.vkclient.utils.GsonAdapter.getFriendsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getGroupFromJson;
import static com.my.vkclient.utils.GsonAdapter.getNewsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getUserFromJson;

public class VkRepository {
    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
//    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";
    public static final String ACCESS_TOKEN = "&v=5.92&access_token=";
    public static final String API_VK_RESPONSE_ACCESS_DENIED_ERROR = "https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied";
    public static final String API_VK_RESPONSE_ACCESS_TOKEN = "https://oauth.vk.com/blank.html#access_token=";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_URL = "https://api.vk.com/method/friends.get?order=name";
    public static final String FRIENDS_COUNT = "&count=";
    public static final String FRIENDS_OFFSET = "&offset=";
    public static final String FRIENDS_FIELDS = "&fields=photo_100,online,photo_max_orig,crop_photo";
    public static final String API_VK_GET_USER_URL = "https://api.vk.com/method/users.get?name_case=nom";
    public static final String USER_ID = "&user_ids=";
    public static final String USER_FIELDS = "&fields=photo_100,photo_max_orig,crop_photo";
    public static final String API_VK_GET_NEWS_URL = "https://api.vk.com/method/newsfeed.get?filters=post,photo,photo_tag";
    public static final String NEWS_START_FROM = "&start_from=";
    public static final String NEWS_COUNT = "&count=";
    public static final String API_VK_GET_GROUP_URL = "https://api.vk.com/method/groups.getById?group_id=";
//    public static final String GROUP_FIELDS = "&fields=";
    private static final Executor executor = Executors.newCachedThreadPool();
    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
    }

    public static void getUserById(final int userId, final ResultCallback<User> resultCallback) {
        getFromUrl(getUserRequest(userId), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(getUserFromJson(result));
            }
        });
    }

    public static void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        getFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(getFriendsFromJson(result));
            }
        });
    }

    public static void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
        getFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(getNewsFromJson(result));
            }
        });
    }

    public static void getGroupById(final int groupId, final ResultCallback<Group> resultCallback) {
        getFromUrl(getGroupRequest(groupId), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(getGroupFromJson(result));
            }
        });
    }

    private static void getFromUrl(final String requestUrl, final ResultCallback<String> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(requestUrl);
                    String result = readStream(url.openStream());
                    resultCallback.onResult(result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static String readStream(InputStream inputStream) {
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                resultOutputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return resultOutputStream.toString();
    }

    private static String getFriendsRequest(int startPosition, int size) {
        return new StringBuilder()
                .append(API_VK_GET_FRIENDS_URL)
                .append(FRIENDS_COUNT)
                .append(size)
                .append(FRIENDS_OFFSET)
                .append(startPosition)
                .append(FRIENDS_FIELDS)
                .append(ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getUserRequest(int userId) {
        return new StringBuilder()
                .append(API_VK_GET_USER_URL)
                .append(USER_ID)
                .append(userId)
                .append(USER_FIELDS)
                .append(ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getNewsRequest(String startFrom, int size) {
        return new StringBuilder()
                .append(API_VK_GET_NEWS_URL)
                .append(NEWS_START_FROM)
                .append(startFrom)
                .append(NEWS_COUNT)
                .append(size)
                .append(ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getGroupRequest(int userId) {
        return new StringBuilder()
                .append(API_VK_GET_GROUP_URL)
                .append(userId)
                .append(ACCESS_TOKEN)
                .append(accessToken).toString();
    }
}
