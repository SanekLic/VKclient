package com.my.vkclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PAGE_SIZE = 10;
    //public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
    public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";

    public static String accessToken;
    String TAG = "loadMoreItems";
    private WebView loginWebView;
    private RecyclerView friendsRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
    private boolean isLoading;
    private boolean isLoadComplete;
    //    private FriendListUpdater friendListUpdater;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginWebView = findViewById(R.id.loginWebView);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);

        setupRecyclerView();
        setupLoginWebView();
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter();
        friendsRecyclerView.setAdapter(friendRecyclerViewAdapter);
        friendsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                validateLoadMoreItems();
            }
        });
//        friendListUpdater = new FriendListUpdater(friendsRecyclerView, friendRecyclerViewAdapter);
    }

    private void setupLoginWebView() {
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

                    }
                });
                loadMoreItems(0, PAGE_SIZE * 2);
//                friendListUpdater.setAccessToken(accessToken);
//                friendListUpdater.start();
            }
        });
        loginWebView.setWebViewClient(loginWebViewClient);
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

    private void validateLoadMoreItems() {
        if (!isLoading && !isLoadComplete) {
            int totalItemCount = linearLayoutManager.getItemCount();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems(totalItemCount, PAGE_SIZE);
            }
        }
    }

    private void loadMoreItems(final int startPosition, final int size) {
        Log.d(TAG, "loadMoreItems() called with: startPosition = [" + startPosition + "], size = [" + size + "]");
        isLoading = true;
        VkRepository.getFriends(startPosition, size, new ResultCallback<List<Friend>>() {
            @Override
            public void onResult(final List<Friend> resultList) {
                if (resultList != null) {
                    if (resultList.size() < size) {
                        isLoadComplete = true;
                    }

                    friendsRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run() returned: " + resultList.size());
                            friendRecyclerViewAdapter.addItems(resultList);
                        }
                    });
                } else {
                    isLoadComplete = true;
                }

                isLoading = false;
            }
        });
    }
}
