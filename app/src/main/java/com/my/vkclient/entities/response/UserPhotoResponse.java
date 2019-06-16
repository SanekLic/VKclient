package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;
import com.my.vkclient.entities.Photo;

import java.util.List;

public class UserPhotoResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("items")
        private List<Photo> userPhotos;

        public List<Photo> getUserPhotos() {
            return userPhotos;
        }
    }
}
