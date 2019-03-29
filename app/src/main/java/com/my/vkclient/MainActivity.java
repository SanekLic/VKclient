package com.my.vkclient;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";

    public static String accessToken;

    private WebView loginWebView;
    private RecyclerView friendsRecyclerView;
//    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
//    private FriendListUpdater friendListUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginWebView = findViewById(R.id.loginWebView);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // friendRecyclerViewAdapter = new FriendRecyclerViewAdapter();


//        friendsRecyclerView.setAdapter(friendRecyclerViewAdapter);
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient(new AccessGrantedCallback() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                accessToken = receivedAccessToken;
                loginWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        loginWebView.setVisibility(View.GONE);
                    }
                });
                friendsRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        friendsRecyclerView.setVisibility(View.VISIBLE);

                        FriendPositionalDataSource friendPositionalDataSource = new FriendPositionalDataSource();

                        PagedList.Config config = new PagedList.Config.Builder()
                                .setEnablePlaceholders(false)
                                .setPageSize(10)
                                .build();

                        PagedList<Friend> pagedList = new PagedList.Builder<>(friendPositionalDataSource, config)
                                .setNotifyExecutor(new Executor() {
                                    private final Handler mHandler = new Handler(Looper.getMainLooper());

                                    @Override
                                    public void execute(Runnable command) {
                                        mHandler.post(command);
                                    }
                                })
                                .setFetchExecutor(Executors.newSingleThreadExecutor())
                                .build();

                        FriendPagedListAdapter friendPagedListAdapter = new FriendPagedListAdapter();
                        friendPagedListAdapter.submitList(pagedList);
                        friendsRecyclerView.setAdapter(friendPagedListAdapter);
                    }
                });

//                friendListUpdater.setAccessToken(accessToken);
//                friendListUpdater.start();
            }
        });
        loginWebView.setWebViewClient(loginWebViewClient);
//        friendListUpdater = new FriendListUpdater(friendsRecyclerView, friendRecyclerViewAdapter);

        loginWebView.loadUrl(API_VK_GET_AUTHORIZE_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        friendListUpdater.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        friendListUpdater.stop();
    }
}
