package com.my.vkclient.entities;

import java.util.List;

public class Photo {
    private int id;
    private List<Size> sizes;

    public int getId() {
        return id;
    }

    public List<Size> getSizes() {
        return sizes;
    }
}
