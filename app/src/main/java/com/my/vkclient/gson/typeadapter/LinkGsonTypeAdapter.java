package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Link;
import com.my.vkclient.gson.GsonHelper;

import java.lang.reflect.Type;

public class LinkGsonTypeAdapter implements JsonDeserializer<Link> {

    @Override
    public Link deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Link fromJson = GsonHelper.getInstance().getGsonWithPhotoTypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            if (fromJson.getPhoto() != null) {
                fromJson.setPhotoUrl(fromJson.getPhoto().getPhotoUrl());
                fromJson.setPhotoHeight(fromJson.getPhoto().getPhotoHeight());
                fromJson.setPhotoWidth(fromJson.getPhoto().getPhotoWidth());
            }

            return fromJson;
        }

        return null;
    }
}
