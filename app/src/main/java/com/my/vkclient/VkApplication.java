package com.my.vkclient;

import android.app.Application;

import com.my.vkclient.Repository.VkRepository;

public class VkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        VkRepository.initializeDatabaseHelper(getApplicationContext(), null, Constants.Database.DATABASE_VERSION);
    }
}
