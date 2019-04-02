package com.my.vkclient.entities;

import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    private int id;
    private List<Size> sizes;

    public int getId() {
        return id;
    }

    public List<Size> getSizes() {
        return sizes;
    }
}
