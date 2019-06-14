package com.my.vkclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
import static com.my.vkclient.Constants.SharedPreferences.APP_SETTINGS;

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesHelper();
        }

        return instance;
    }

    public void initializeContext(@NonNull final Context context) {
        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
    }
}
