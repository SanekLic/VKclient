package com.my.vkclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
import static com.my.vkclient.Constants.STRING_EMPTY;
import static com.my.vkclient.Constants.SharedPreferences.ACCESS_TOKEN_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.ANIMATION_ENABLE_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.APP_SETTINGS;
import static com.my.vkclient.Constants.SharedPreferences.NEWS_START_FROM_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.PROFILE_ID_SHARED_KEY;

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;
    private final Object editorChanged = new Object();
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private String newsStartFrom;
    private boolean animationEnable;
    private int profileId;

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesHelper();
        }

        return instance;
    }

    public void initializeContext(@NonNull final Context context) {
        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);

        accessToken = sharedPreferences.getString(ACCESS_TOKEN_SHARED_KEY, null);
        newsStartFrom = sharedPreferences.getString(NEWS_START_FROM_SHARED_KEY, STRING_EMPTY);
        animationEnable = sharedPreferences.getBoolean(ANIMATION_ENABLE_SHARED_KEY, true);
        profileId = sharedPreferences.getInt(PROFILE_ID_SHARED_KEY, 0);
    }

    public void clear() {
        accessToken = null;
        newsStartFrom = STRING_EMPTY;
        animationEnable = true;
        profileId = 0;

        synchronized (editorChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;

        synchronized (editorChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PROFILE_ID_SHARED_KEY, profileId);
            editor.apply();
        }
    }

    public String getAccessToke() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        synchronized (editorChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ACCESS_TOKEN_SHARED_KEY, accessToken);
            editor.apply();
        }
    }

    public String getNewsStartFrom() {
        return newsStartFrom;
    }

    public void setNewsStartFrom(String newsStartFrom) {
        this.newsStartFrom = newsStartFrom;

        synchronized (editorChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NEWS_START_FROM_SHARED_KEY, newsStartFrom);
            editor.apply();
        }
    }

    public boolean isAnimationEnable() {
        return animationEnable;
    }

    public void setAnimationEnable(boolean animationEnable) {
        this.animationEnable = animationEnable;

        synchronized (editorChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ANIMATION_ENABLE_SHARED_KEY, animationEnable);
            editor.apply();
        }
    }
}
