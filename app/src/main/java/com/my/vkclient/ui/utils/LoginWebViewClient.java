package com.my.vkclient.ui.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.vkclient.VkRepository;

public class LoginWebViewClient extends WebViewClient {
    private LoginWebViewCallback loginWebViewCallback;

    public LoginWebViewClient(LoginWebViewCallback loginWebViewCallback) {
        this.loginWebViewCallback = loginWebViewCallback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith(VkRepository.API_VK_RESPONSE_ACCESS_DENIED_ERROR) && url.endsWith("state=requestToken")) {
            url = VkRepository.API_VK_GET_AUTHORIZE_URL;
        }

        if (url.startsWith(VkRepository.API_VK_RESPONSE_ACCESS_TOKEN)) {
            String access_token = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
            loginWebViewCallback.onAccessGranted(access_token);

            return false;
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
