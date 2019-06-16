package com.my.vkclient.entities.response;

import com.google.gson.annotations.SerializedName;
import com.my.vkclient.entities.Group;

import java.util.List;

public class UserGroupResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        @SerializedName("items")
        private List<Group> groupList;

        public List<Group> getGroupList() {
            return groupList;
        }
    }
}
