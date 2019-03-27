package com.my.vkclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Friend {
    private int id;
    private String first_name;
    private String last_name;
    private String photo_50;
    private String photo_100;
    private String photo_200_orig;
    private int online;

    public Friend(JSONObject jsonUserInFriends) {
        try {
            id = jsonUserInFriends.getInt("id");
            first_name = jsonUserInFriends.getString("first_name");
            last_name = jsonUserInFriends.getString("last_name");
            photo_50 = jsonUserInFriends.getString("photo_50");
            photo_100 = jsonUserInFriends.getString("photo_100");
            photo_200_orig = jsonUserInFriends.getString("photo_200_orig");
            online = jsonUserInFriends.getInt("online");
        } catch (JSONException e) {
        }
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public String getPhoto_200_orig() {
        return photo_200_orig;
    }

    public int getOnline() {
        return online;
    }

    public boolean equals(Friend object) {
        return this.compare(object).isEmpty();
    }

    public ArrayList<FriendDifferences> compare(Friend object) {
        ArrayList<FriendDifferences> differences = new ArrayList<>();

        if (getId() != object.getId()) {
            differences.add(FriendDifferences.DIFFERENT_ID);
        }

        if (!getFirst_name().equals(object.getFirst_name())) {
            differences.add(FriendDifferences.DIFFERENT_FIRST_NAME);
        }

        if (!getLast_name().equals(object.getLast_name())) {
            differences.add(FriendDifferences.DIFFERENT_LAST_NAME);
        }

        if (!getPhoto_50().equals(object.getPhoto_50())) {
            differences.add(FriendDifferences.DIFFERENT_PHOTO_50);
        }

        if (!getPhoto_100().equals(object.getPhoto_100())) {
            differences.add(FriendDifferences.DIFFERENT_PHOTO_100);
        }

        if (!getPhoto_200_orig().equals(object.getPhoto_200_orig())) {
            differences.add(FriendDifferences.DIFFERENT_PHOTO_200_ORIG);
        }

        if (getOnline() != object.getOnline()) {
            differences.add(FriendDifferences.DIFFERENT_ONLINE);
        }

        return differences;
    }

    public enum FriendDifferences {
        DIFFERENT_ID,
        DIFFERENT_FIRST_NAME,
        DIFFERENT_LAST_NAME,
        DIFFERENT_PHOTO_50,
        DIFFERENT_PHOTO_100,
        DIFFERENT_PHOTO_200_ORIG,
        DIFFERENT_ONLINE
    }
}
