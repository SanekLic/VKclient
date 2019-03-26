package com.my.vkclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static List<Friend> importFriendsFromJson(String jsonFriends) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFriends);
            JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items");
            List<Friend> friends = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                friends.add(new Friend(jsonArray.getJSONObject(i)));
            }

            return friends;
        } catch (JSONException e) {
            return null;
        }
    }
}
