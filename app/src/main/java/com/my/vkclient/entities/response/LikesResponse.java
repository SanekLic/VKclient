package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;

public class LikesResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("likes")
        private Integer likesCount;

        public Integer getLikesCount() {
            return likesCount;
        }
    }
}
