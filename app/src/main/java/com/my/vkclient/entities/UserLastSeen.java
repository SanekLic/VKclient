package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class UserLastSeen {
    @SerializedName("time")
    private long time;

    public UserLastSeen(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
