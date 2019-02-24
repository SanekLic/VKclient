package com.my.vkclient;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    //public static String authorizeURL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
    public static String authorizeURL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications,offline&response_type=token&v=5.92&state=requestToken";

    public String access_token;

    private WebView loginWebView;
    private ConstraintLayout friendsLayout;
    private RecyclerView friendsRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;

    private List<UserInFriends> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginWebView = findViewById(R.id.loginWebView);
        friendsLayout = findViewById(R.id.friendsLayout);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter();
        friendsRecyclerView.setAdapter(friendRecyclerViewAdapter);

        LoginWebViewClient loginWebViewClient = new LoginWebViewClient();
        loginWebView.setWebViewClient(loginWebViewClient);

        loginWebViewClient.addAccessGrantedListener(new LoginWebViewClient.AccessGrantedListener() {
            @Override
            public void onAccessGranted(String received_access_token) {
                access_token = received_access_token;
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
            }
        });

        loginWebView.loadUrl(authorizeURL);
    }

    public void onClickButton(View view) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(new StringBuilder().append("https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=").append(access_token).toString());
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    readStream(inputStream);
                } catch (IOException e) {
                }
            }
        }).start();
    }

    private void readStream(InputStream inputStream) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            friends = importFriendsFromJson(result.toString());

            friendsRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    friendRecyclerViewAdapter.setItems(friends);
                }
            });
        } catch (IOException e) {
        }
    }
}
