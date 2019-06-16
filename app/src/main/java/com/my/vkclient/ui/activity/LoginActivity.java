package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.webview.LoginWebViewCallback;
import com.my.vkclient.ui.webview.LoginWebViewClient;

public class LoginActivity extends AppCompatActivity {
    private WebView loginWebView;
    private ProgressBar loginProgressBar;
    private TextView networkConnectionErrorTextView;
    private Button repeatedConnectionButton;

    public static void show(final Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VkRepository.getInstance().isOfflineAccess()) {
            MainActivity.show(LoginActivity.this);

            finish();
        } else {
            setContentView(R.layout.activity_login);
            setupView();
            setupLoginWebView();
        }
    }

    private void setupView() {
        loginWebView = findViewById(R.id.loginWebView);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        networkConnectionErrorTextView = findViewById(R.id.networkConnectionErrorTextView);
        repeatedConnectionButton = findViewById(R.id.repeatedConnectionButton);
        repeatedConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWebView.loadUrl(Constants.API_VK.API_VK_GET_AUTHORIZE_URL);
                loginProgressBar.setVisibility(View.VISIBLE);
                networkConnectionErrorTextView.setVisibility(View.GONE);
                repeatedConnectionButton.setVisibility(View.GONE);
            }
        });
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
            public void onFinishLoading(boolean errorConnection) {
                loginProgressBar.setVisibility(View.GONE);

                if (errorConnection) {
                    if (loginWebView.getVisibility() != View.GONE) {
                        loginWebView.setVisibility(View.GONE);
                    }

                    if (networkConnectionErrorTextView.getVisibility() != View.VISIBLE) {
                        networkConnectionErrorTextView.setVisibility(View.VISIBLE);
                        repeatedConnectionButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (loginWebView.getVisibility() != View.VISIBLE) {
                        loginWebView.setVisibility(View.VISIBLE);
                    }

                    if (networkConnectionErrorTextView.getVisibility() != View.GONE) {
                        networkConnectionErrorTextView.setVisibility(View.GONE);
                        repeatedConnectionButton.setVisibility(View.GONE);
                    }
                }
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
