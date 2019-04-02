package com.my.vkclient.entities;

import java.util.List;

public class FriendResponse {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {

        private int count;
        private List<Friend> items;

        public int getCount() {
            return count;
        }

        public List<Friend> getItems() {
            return items;
        }
    }
}
