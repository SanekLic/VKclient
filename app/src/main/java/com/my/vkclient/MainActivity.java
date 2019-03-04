package com.my.vkclient;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friendsList,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";

    public static String accessToken;

    private WebView loginWebView;
    private ConstraintLayout friendsLayout;
    private RecyclerView friendsRecyclerView;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private FriendsListUpdater friendsListUpdater;

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
        friendsListUpdater = new FriendsListUpdater(friendsRecyclerView, friendsRecyclerViewAdapter);

        loginWebViewClient.addAccessGrantedListener(new LoginWebViewClient.AccessGrantedListener() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                accessToken = receivedAccessToken;
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

                friendsListUpdater.setAccessToken(accessToken);
                friendsListUpdater.start();
            }
        });
        loginWebView.loadUrl(API_VK_GET_AUTHORIZE_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        friendsListUpdater.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        friendsListUpdater.stop();
    }

    public void onClickButton(View view) {
    }
}
