package com.my.vkclient.Entities;
import java.util.ArrayList;

public class Friend {
    private int id;
    private String first_name;
    private String last_name;
    private String photo_100;
    private int online;

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public int getOnline() {
        return online;
    }

    public boolean equals(Friend object) {
        return this.compare(object).isEmpty();
    }

    public ArrayList<FriendDifferences> compare(Friend object) {
        ArrayList<FriendDifferences> differences = new ArrayList<>();

        if (getId() != object.getId()) {
            differences.add(FriendDifferences.DIFFERENT_ID);
        }

        if (!getFirst_name().equals(object.getFirst_name())) {
            differences.add(FriendDifferences.DIFFERENT_FIRST_NAME);
        }

        if (!getLast_name().equals(object.getLast_name())) {
            differences.add(FriendDifferences.DIFFERENT_LAST_NAME);
        }

        if (!getPhoto_100().equals(object.getPhoto_100())) {
            differences.add(FriendDifferences.DIFFERENT_PHOTO_100);
        }

        if (getOnline() != object.getOnline()) {
            differences.add(FriendDifferences.DIFFERENT_ONLINE);
        }

        return differences;
    }

    public enum FriendDifferences {
        DIFFERENT_ID,
        DIFFERENT_FIRST_NAME,
        DIFFERENT_LAST_NAME,
        DIFFERENT_PHOTO_100,
        DIFFERENT_ONLINE
    }
}
