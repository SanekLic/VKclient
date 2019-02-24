package com.my.vkclient;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInFriends {

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
        }catch (JSONException e){
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
}
