package com.my.vkclient.entities;

import java.io.Serializable;

public class Size implements Serializable {
    private String type;
    private String url;
    private int width;
    private int height;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
