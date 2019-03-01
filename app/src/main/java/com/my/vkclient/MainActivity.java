package com.my.vkclient;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.my.vkclient.JsonHelper.importFriendsFromJson;

public class MainActivity extends AppCompatActivity {

    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friendsList,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";

    public String access_token;

    private WebView loginWebView;
    private ConstraintLayout friendsLayout;
    private RecyclerView friendsRecyclerView;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;

    private List<UserInFriends> friendsList;

    private volatile boolean runFriendsListUpdateThread;

    private Thread friendsListUpdateThread;
    private Runnable friendsListUpdateRunnable = new Runnable() {
        public void run() {
            while (runFriendsListUpdateThread) {
                try {
                    URL url = new URL(new StringBuilder()
                            .append(API_VK_GET_FRIENDS_LIST_URL)
                            .append(access_token).toString());
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginWebView = findViewById(R.id.loginWebView);
        friendsLayout = findViewById(R.id.friendsLayout);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter();
        friendsRecyclerView.setAdapter(friendsRecyclerViewAdapter);
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient();
        loginWebView.setWebViewClient(loginWebViewClient);

        loginWebViewClient.addAccessGrantedListener(new LoginWebViewClient.AccessGrantedListener() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                access_token = receivedAccessToken;
                loginWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        loginWebView.setVisibility(View.GONE);
                    }
                });
                friendsLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        friendsLayout.setVisibility(View.VISIBLE);
                    }
                });

                if (friendsListUpdateThread == null) {
                    runFriendsListUpdateThread = true;
                    friendsListUpdateThread = new Thread(friendsListUpdateRunnable);
                    friendsListUpdateThread.start();
                }
            }
        });
        loginWebView.loadUrl(API_VK_GET_AUTHORIZE_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (access_token != null && friendsListUpdateThread == null) {
            runFriendsListUpdateThread = true;
            friendsListUpdateThread = new Thread(friendsListUpdateRunnable);
            friendsListUpdateThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (friendsListUpdateThread != null) {
            runFriendsListUpdateThread = false;
            friendsListUpdateThread = null;
        }
    }

    public void onClickButton(View view) {
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
