package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.gson.GsonHelper;

import java.lang.reflect.Type;

public class PodcastGsonTypeAdapter implements JsonDeserializer<Podcast> {

    @Override
    public Podcast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Podcast fromJson = GsonHelper.getInstance().getGsonWithOnlyPhotoTypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            if (fromJson.getPodcastInfo() != null && fromJson.getPodcastInfo().getCover() != null) {
                fromJson.setPhotoUrl(fromJson.getPodcastInfo().getCover().getPhotoUrl());
                fromJson.setPhotoHeight(fromJson.getPodcastInfo().getCover().getPhotoHeight());
                fromJson.setPhotoWidth(fromJson.getPodcastInfo().getCover().getPhotoWidth());
            }

            return fromJson;
        }

        return null;
    }
}
