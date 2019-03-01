package com.my.vkclient;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import static com.my.vkclient.MainActivity.API_VK_GET_AUTHORIZE_URL;

public class LoginWebViewClient extends WebViewClient {

    private ArrayList<AccessGrantedListener> mListeners;

    public interface AccessGrantedListener {
        void onAccessGranted(String receivedAccessToken);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        //error on click cancel
        //https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied&error_description=User%20denied%20your%20request&state=requestToken
        if (url.startsWith("https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied") && url.endsWith("state=requestToken")) {
            url = API_VK_GET_AUTHORIZE_URL;
        }

        if (url.startsWith("https://oauth.vk.com/blank.html#access_token=")) {
            String access_token = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
            for (AccessGrantedListener listener : mListeners) {
                listener.onAccessGranted(access_token);
            }

            return false;
        }
        view.loadUrl(url);

        return true;
    }

    public void addAccessGrantedListener(AccessGrantedListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }

        mListeners.add(listener);
    }

    public void removeTextChangedListener(AccessGrantedListener listener) {
        if (mListeners != null) {
            int i = mListeners.indexOf(listener);

            if (i >= 0) {
                mListeners.remove(i);
            }
        }
    }
}
