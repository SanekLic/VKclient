package com.my.vkclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInFriends {
    public enum FriendDifferences {
        different_id,
        different_first_name,
        different_last_name,
        different_photo_50,
        different_photo_100,
        different_photo_200_orig,
        different_online
    }

    private int id;
    private String first_name;
    private String last_name;
    private String photo_50;
    private String photo_100;
    private String photo_200_orig;
    private int online;

    public UserInFriends(JSONObject jsonUserInFriends) {
        try {
            this.id = jsonUserInFriends.getInt("id");
            this.first_name = jsonUserInFriends.getString("first_name");
            this.last_name = jsonUserInFriends.getString("last_name");
            this.photo_50 = jsonUserInFriends.getString("photo_50");
            this.photo_100 = jsonUserInFriends.getString("photo_100");
            this.photo_200_orig = jsonUserInFriends.getString("photo_200_orig");
            this.online = jsonUserInFriends.getInt("online");
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

    public boolean equals(UserInFriends object) {
        return this.getId() == object.getId();
    }

    public ArrayList compare(UserInFriends object) {
        ArrayList<FriendDifferences> differences = new ArrayList<>();
        if (this.getId() != object.getId()) {
            differences.add(FriendDifferences.different_id);
        }
        if (!this.getFirst_name().equals(object.getFirst_name())) {
            differences.add(FriendDifferences.different_first_name);
        }
        if (!this.getLast_name().equals(object.getLast_name())) {
            differences.add(FriendDifferences.different_last_name);
        }
        if (!this.getPhoto_50().equals(object.getPhoto_50())) {
            differences.add(FriendDifferences.different_photo_50);
        }
        if (!this.getPhoto_100().equals(object.getPhoto_100())) {
            differences.add(FriendDifferences.different_photo_100);
        }
        if (!this.getPhoto_200_orig().equals(object.getPhoto_200_orig())) {
            differences.add(FriendDifferences.different_photo_200_orig);
        }
        if (this.getOnline() != object.getOnline()) {
            differences.add(FriendDifferences.different_online);
        }

        return differences;
    }
}
