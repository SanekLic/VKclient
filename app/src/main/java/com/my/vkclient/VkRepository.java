package com.my.vkclient;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;
import static com.my.vkclient.MainActivity.accessToken;

public class VkRepository {
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
                .append("https://api.vk.com/method/friends.get?order=name&count=")
                .append(size)
                .append("&offset=")
                .append(startPosition)
                .append("&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=")
                .append(accessToken).toString();
    }
}
