package com.my.vkclient;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;

class FriendListUpdater {
    private volatile boolean runFriendListUpdateThread;
    private String accessToken;

    private RecyclerView friendsRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
    private List<Friend> friendList;
    private Thread friendListUpdateThread;
    private Runnable friendListUpdateRunnable = new Runnable() {
        public void run() {
            while (runFriendListUpdateThread) {
                try {
                    URL url = new URL(new StringBuilder()
//                            .append(API_VK_GET_FRIENDS_LIST_URL)
                            .append(accessToken).toString());
                    InputStream inputStream = url.openStream();
                    readStream(inputStream);
                    Thread.sleep(5000);
                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    runFriendListUpdateThread = false;
                }
            }
        }
    };

    FriendListUpdater(RecyclerView friendsRecyclerView, FriendRecyclerViewAdapter friendRecyclerViewAdapter) {
        this.friendsRecyclerView = friendsRecyclerView;
        this.friendRecyclerViewAdapter = friendRecyclerViewAdapter;
    }

    public void start() {
        if (accessToken != null && friendListUpdateThread == null) {
            runFriendListUpdateThread = true;
            friendListUpdateThread = new Thread(friendListUpdateRunnable);
            friendListUpdateThread.start();
        }
    }

    public void stop() {
        if (friendListUpdateThread != null) {
            runFriendListUpdateThread = false;
            friendListUpdateThread = null;
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

            friendList = importFriendsFromJson(result.toString());

            if (friendList != null) {
                friendsRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        friendRecyclerViewAdapter.setItems(friendList);
                    }
                });
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}
