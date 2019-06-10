package com.my.vkclient.ui.adapters;

public interface LoginWebViewCallback {
    void onAccessGranted(String receivedAccessToken);
    void onLoadingFinished();
    void onCancelLogin();
}
