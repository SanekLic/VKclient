package com.my.vkclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.my.vkclient.ui.utils.LoginWebViewCallback;
import com.my.vkclient.R;
import com.my.vkclient.VkRepository;
import com.my.vkclient.ui.utils.LoginWebViewClient;

public class LoginActivity extends AppCompatActivity {
    private WebView loginWebView;
    private ProgressBar loginProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginWebView = findViewById(R.id.loginWebView);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        setupLoginWebView();
    }

    private void setupLoginWebView() {
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient(new LoginWebViewCallback() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                VkRepository.setAccessToken(receivedAccessToken);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }

            @Override
            public void onLoadingFinished() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginWebView.setVisibility(View.VISIBLE);
                        loginProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        loginWebView.setWebViewClient(loginWebViewClient);
        loginWebView.loadUrl(VkRepository.API_VK_GET_AUTHORIZE_URL);
    }
}
