package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("count")
        private int count;

        @SerializedName("items")
        private List<User> userList;

        public int getCount() {
            return count;
        }

        public List<User> getUserList() {
            return userList;
        }
    }
}
