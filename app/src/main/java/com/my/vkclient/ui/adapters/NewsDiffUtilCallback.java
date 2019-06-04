package com.my.vkclient.ui.adapters;

import android.support.v7.util.DiffUtil;

import com.my.vkclient.entities.News;

import java.util.ArrayList;
import java.util.List;

public class NewsDiffUtilCallback extends DiffUtil.Callback {
    private List<News> oldNewsList;
    private List<News> newNewsList;

    public NewsDiffUtilCallback() {
        oldNewsList = new ArrayList<>();
        newNewsList = new ArrayList<>();
    }

    @Override
    public int getOldListSize() {
        return oldNewsList.size();
    }

    @Override
    public int getNewListSize() {
        return newNewsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNewsList.get(oldItemPosition).equals(newNewsList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNewsList.get(oldItemPosition).equals(newNewsList.get(newItemPosition));
    }

    public NewsDiffUtilCallback setOldList(List<News> oldNewsList) {
        this.oldNewsList.clear();
        this.oldNewsList.addAll(oldNewsList);

        return this;
    }

    public NewsDiffUtilCallback setNewList(List<News> newNewsList) {
        this.newNewsList.clear();
        this.newNewsList.addAll(newNewsList);

        return this;
    }
}