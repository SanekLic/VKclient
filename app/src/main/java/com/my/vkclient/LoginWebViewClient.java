package com.my.vkclient;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginWebViewClient extends WebViewClient {
    private AccessGrantedCallback accessGrantedCallback;

    public LoginWebViewClient(AccessGrantedCallback accessGrantedCallback) {
        this.accessGrantedCallback = accessGrantedCallback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith(VkRepository.API_VK_RESPONSE_ACCESS_DENIED_ERROR) && url.endsWith("state=requestToken")) {
            url = VkRepository.API_VK_GET_AUTHORIZE_URL;
        }

        if (url.startsWith(VkRepository.API_VK_RESPONSE_ACCESS_TOKEN)) {
            String access_token = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
            accessGrantedCallback.onAccessGranted(access_token);

            return false;
        }

        view.loadUrl(url);

        return true;
    }
}
