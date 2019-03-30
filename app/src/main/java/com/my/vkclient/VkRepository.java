package com.my.vkclient;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;

public class VkRepository {
    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
//    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";
    public static final String API_VK_RESPONSE_ACCESS_DENIED_ERROR = "https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied";
    public static final String API_VK_RESPONSE_ACCESS_TOKEN = "https://oauth.vk.com/blank.html#access_token=";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_URL = "https://api.vk.com/method/friends.get?order=name";
    public static final String COUNT = "&count=";
    public static final String OFFSET = "&offset=";
    public static final String FIELDS = "&fields=photo_50,photo_100,photo_200_orig,online&v=5.92";
    public static final String ACCESS_TOKEN = "&access_token=";

    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
    }

    public static void getFriends(final int startPosition, final int size, final ResultCallback<List<Friend>> ResultCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    URL url = new URL(getFriendRequest(startPosition, size));
                    inputStream = url.openStream();
                    String jsonFriends = readStream(inputStream);
                    ResultCallback.onResult(importFriendsFromJson(jsonFriends));
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

    private static String getFriendRequest(int startPosition, int size) {
        return new StringBuilder()
                .append(API_VK_GET_FRIENDS_URL)
                .append(COUNT)
                .append(size)
                .append(OFFSET)
                .append(startPosition)
                .append(FIELDS)
                .append(ACCESS_TOKEN)
                .append(accessToken).toString();
    }
}
