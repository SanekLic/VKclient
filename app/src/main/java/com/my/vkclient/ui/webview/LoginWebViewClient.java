package com.my.vkclient.ui.webview;

import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.vkclient.Constants;

public class LoginWebViewClient extends WebViewClient {
    private LoginWebViewCallback loginWebViewCallback;
    private boolean errorConnection;

    public LoginWebViewClient(LoginWebViewCallback loginWebViewCallback) {
        this.loginWebViewCallback = loginWebViewCallback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(Constants.API_VK.API_VK_RESPONSE_ACCESS_DENIED_ERROR) && url.endsWith(Constants.API_VK.STATE_REQUEST_TOKEN)) {
            loginWebViewCallback.onCancelLogin();

            return true;
        }

        if (url.startsWith(Constants.API_VK.API_VK_RESPONSE_ACCESS_TOKEN)) {
            String access_token = url.substring(url.indexOf(Constants.STRING_EQUALS) + 1, url.indexOf(Constants.STRING_AND));
            loginWebViewCallback.onAccessGranted(access_token);

            return true;
        }

        view.loadUrl(url);

        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

        errorConnection = true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        loginWebViewCallback.onFinishLoading(errorConnection);
        errorConnection = false;
    }
}
