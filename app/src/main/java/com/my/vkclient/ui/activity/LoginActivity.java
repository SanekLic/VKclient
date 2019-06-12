package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.LoginWebViewCallback;
import com.my.vkclient.ui.LoginWebViewClient;

public class LoginActivity extends AppCompatActivity {
    private WebView loginWebView;
    private ProgressBar loginProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VkRepository.getInstance().isOfflineAccess()) {
            MainActivity.show(LoginActivity.this);

            finish();
        } else {
            setContentView(R.layout.activity_login);
            loginWebView = findViewById(R.id.loginWebView);
            loginProgressBar = findViewById(R.id.loginProgressBar);

            setupLoginWebView();
        }
    }

    public static void show(final Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void setupLoginWebView() {
        LoginWebViewClient loginWebViewClient = new LoginWebViewClient(new LoginWebViewCallback() {
            @Override
            public void onAccessGranted(String receivedAccessToken) {
                VkRepository.getInstance().setAccessToken(receivedAccessToken);
                MainActivity.show(LoginActivity.this);

                finish();
            }

            @Override
            public void onFinishLoading() {
                loginWebView.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelLogin() {
                finishAffinity();
            }
        });

        loginWebView.setWebViewClient(loginWebViewClient);
        loginWebView.loadUrl(Constants.API_VK.API_VK_GET_AUTHORIZE_URL);
    }
}
