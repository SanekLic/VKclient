package com.my.vkclient;

import android.app.Application;

import com.my.vkclient.repository.VkRepository;

public class VkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        VkRepository.getInstance().initialContext(getApplicationContext());
    }
}
