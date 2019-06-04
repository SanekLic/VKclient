package com.my.vkclient.ui.adapters;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.vkclient.Constants;

public class LoginWebViewClient extends WebViewClient {
    private LoginWebViewCallback loginWebViewCallback;

    public LoginWebViewClient(LoginWebViewCallback loginWebViewCallback) {
        this.loginWebViewCallback = loginWebViewCallback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith(Constants.API_VK.API_VK_RESPONSE_ACCESS_DENIED_ERROR) && url.endsWith(Constants.API_VK.STATE_REQUEST_TOKEN)) {
            url = Constants.API_VK.API_VK_GET_AUTHORIZE_URL;
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
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        loginWebViewCallback.onLoadingFinished();
    }
}
