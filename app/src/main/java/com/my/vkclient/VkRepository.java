package com.my.vkclient;

import android.util.Log;

import com.my.vkclient.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;
import static com.my.vkclient.JsonHelper.importUserFromJson;

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
    public static final String FRIENDS_FIELDS = "&fields=photo_100,online";
    public static final String API_VK_GET_USER_URL = "https://api.vk.com/method/users.get?name_case=nom";
    public static final String USER_ID = "&user_ids=";
    public static final String USER_FIELDS = "&fields=photo_max_orig,crop_photo";

    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
    }

    public static void getUser(final int userId, final ResultCallback<User> resultCallback) {
        getFromUrl(getUserRequest(userId), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(importUserFromJson(result));
            }
        });
    }

    public static void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        getFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(importFriendsFromJson(result));
            }
        });
    }

    private static void getFromUrl(final String stringUrl, final ResultCallback<String> resultCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(stringUrl);
                    String jsonFriends = readStream(url.openStream());
                    resultCallback.onResult(jsonFriends);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
}
