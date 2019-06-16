package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;
import com.my.vkclient.entities.User;

import java.util.List;

public class FriendResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("items")
        private List<User> userList;

        public List<User> getUserList() {
            return userList;
        }
    }
}
