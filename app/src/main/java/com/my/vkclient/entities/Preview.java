package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class Preview {
    @SerializedName("photo")
    private Photo photo;

    public Photo getPhoto() {
        return photo;
    }
}
