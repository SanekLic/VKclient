package com.my.vkclient;

import android.app.Application;
import android.os.StrictMode;

import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.SharedPreferencesHelper;

public class VkApplication extends Application {

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate();

        SharedPreferencesHelper.getInstance().initializeContext(this);
        VkRepository.getInstance().initializeContext(this);
    }
}
