package com.my.vkclient;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;
import static com.my.vkclient.MainActivity.API_VK_GET_FRIENDS_LIST_URL;

class FriendsListUpdater {
    private volatile boolean runFriendsListUpdateThread;
    private String accessToken;

    private RecyclerView friendsRecyclerView;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInFriends> friendsList;
    private Thread friendsListUpdateThread;
    private Runnable friendsListUpdateRunnable = new Runnable() {
        public void run() {
            while (runFriendsListUpdateThread) {
                try {
                    URL url = new URL(new StringBuilder()
                            .append(API_VK_GET_FRIENDS_LIST_URL)
                            .append(accessToken).toString());
                    InputStream inputStream = url.openStream();
                    readStream(inputStream);
                    Thread.sleep(5000);
                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    runFriendsListUpdateThread = false;
                }
            }
        }
    };

    FriendsListUpdater(RecyclerView friendsRecyclerView, FriendsRecyclerViewAdapter friendsRecyclerViewAdapter) {
        this.friendsRecyclerView = friendsRecyclerView;
        this.friendsRecyclerViewAdapter = friendsRecyclerViewAdapter;
    }

    public void start() {
        if (accessToken != null && friendsListUpdateThread == null) {
            runFriendsListUpdateThread = true;
            friendsListUpdateThread = new Thread(friendsListUpdateRunnable);
            friendsListUpdateThread.start();
        }
    }

    public void stop() {
        if (friendsListUpdateThread != null) {
            runFriendsListUpdateThread = false;
            friendsListUpdateThread = null;
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private void readStream(InputStream inputStream) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            friendsList = importFriendsFromJson(result.toString());

            if (friendsList != null) {
                friendsRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        friendsRecyclerViewAdapter.setItems(friendsList);
                    }
                });
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}
