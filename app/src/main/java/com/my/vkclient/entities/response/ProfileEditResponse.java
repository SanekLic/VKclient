package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;

public class ProfileEditResponse {
    @SerializedName("response")
    private ProfileEditResponse.Response response;

    public ProfileEditResponse.Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("changed")
        private Boolean changed;

        public Boolean getChanged() {
            return changed;
        }
    }
}
