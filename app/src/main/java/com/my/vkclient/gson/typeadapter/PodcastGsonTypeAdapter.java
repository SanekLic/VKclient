package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.gson.GsonAdapter;

import java.lang.reflect.Type;

public class PodcastGsonTypeAdapter implements JsonDeserializer<Podcast> {

    @Override
    public Podcast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Podcast fromJson = GsonAdapter.getInstance().getGsonWithPhotoRypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            Podcast podcast = new Podcast();

            podcast.setUrl(fromJson.getUrl());
            podcast.setTitle(fromJson.getTitle());

            if (fromJson.getPodcastInfo() != null && fromJson.getPodcastInfo().getCover() != null) {
                podcast.setPhotoUrl(fromJson.getPodcastInfo().getCover().getPhotoUrl());
                podcast.setPhotoHeight(fromJson.getPodcastInfo().getCover().getPhotoHeight());
                podcast.setPhotoWidth(fromJson.getPodcastInfo().getCover().getPhotoWidth());
            }

            return podcast;
        }

        return null;
    }
}
