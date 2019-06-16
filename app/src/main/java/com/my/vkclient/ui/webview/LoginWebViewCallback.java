package com.my.vkclient.ui.webview;

public interface LoginWebViewCallback {
    void onAccessGranted(String receivedAccessToken);
    void onFinishLoading(boolean errorConnection);
    void onCancelLogin();
}
