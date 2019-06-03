package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {
    @SerializedName("response")
    private Response response;

    public NewsResponse() {
        setResponse(new Response());
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        @SerializedName("items")
        private List<News> newsList;
        @SerializedName("profiles")
        private List<User> userList;
        @SerializedName("groups")
        private List<Group> groupList;
        @SerializedName("next_from")
        private String nextFrom;

        Response() {
        }

        public List<News> getNewsList() {
            return newsList;
        }

        public void setNewsList(List<News> newsList) {
            this.newsList = newsList;
        }

        public List<User> getUserList() {
            return userList;
        }

        public List<Group> getGroupList() {
            return groupList;
        }

        public String getNextFrom() {
            return nextFrom;
        }

        public void setNextFrom(String nextFrom) {
            this.nextFrom = nextFrom;
        }
    }
}
