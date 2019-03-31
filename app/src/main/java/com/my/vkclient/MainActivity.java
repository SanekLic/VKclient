package com.my.vkclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView loginWebView;
    private RecyclerView friendRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginWebView = findViewById(R.id.loginWebView);
        friendRecyclerView = findViewById(R.id.friendRecyclerView);

        setupRecyclerView();
        setupLoginWebView();
    }

    private void setupRecyclerView() {
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(friendRecyclerView);
        friendRecyclerView.setAdapter(friendRecyclerViewAdapter);
        friendRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    private void setupLoginWebView() {
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient(new AccessGrantedCallback() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                VkRepository.setAccessToken(receivedAccessToken);

                loginWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        loginWebView.setVisibility(View.GONE);
                    }
                });
                friendRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        friendRecyclerView.setVisibility(View.VISIBLE);
                        friendRecyclerViewAdapter.initialLoadItems();
                        friendRecyclerViewAdapter.autoUpdateItems(true);
                    }
                });
            }
        });
        loginWebView.setWebViewClient(loginWebViewClient);
        loginWebView.loadUrl(VkRepository.API_VK_GET_AUTHORIZE_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (friendRecyclerView.getVisibility() == View.VISIBLE && friendRecyclerViewAdapter != null) {
            friendRecyclerViewAdapter.autoUpdateItems(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (friendRecyclerView.getVisibility() == View.VISIBLE && friendRecyclerViewAdapter != null) {
            friendRecyclerViewAdapter.autoUpdateItems(false);
        }
    }
}
