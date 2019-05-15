package com.my.vkclient.ui.utils;

public interface LoginWebViewCallback {
    void onAccessGranted(String receivedAccessToken);
    void onLoadingFinished();
}
