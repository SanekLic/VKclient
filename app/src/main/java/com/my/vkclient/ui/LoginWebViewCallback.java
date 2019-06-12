package com.my.vkclient.ui;

public interface LoginWebViewCallback {
    void onAccessGranted(String receivedAccessToken);
    void onFinishLoading();
    void onCancelLogin();
}
