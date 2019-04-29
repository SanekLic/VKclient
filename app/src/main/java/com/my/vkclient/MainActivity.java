package com.my.vkclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

import com.my.vkclient.entities.User;

public class MainActivity extends AppCompatActivity {

    public static final String USER_INTENT_KEY = "User";
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(linearLayoutManager);
        friendRecyclerViewAdapter.setOnItemClickListener(new ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra(USER_INTENT_KEY, user);
                startActivity(intent);
            }
        });
        friendRecyclerView.setAdapter(friendRecyclerViewAdapter);
    }

    private void setupLoginWebView() {
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient(new AccessGrantedCallback() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                VkRepository.setAccessToken(receivedAccessToken);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginWebView.setVisibility(View.GONE);

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
