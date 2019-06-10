package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Doc;
import com.my.vkclient.gson.GsonHelper;

import java.lang.reflect.Type;

public class DocGsonTypeAdapter implements JsonDeserializer<Doc> {

    @Override
    public Doc deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Doc fromJson = GsonHelper.getInstance().getGsonWithPhotoTypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            if (fromJson.getPreview() != null && fromJson.getPreview().getPhoto() != null) {
                fromJson.setPhotoUrl(fromJson.getPreview().getPhoto().getPhotoUrl());
                fromJson.setPhotoHeight(fromJson.getPreview().getPhoto().getPhotoHeight());
                fromJson.setPhotoWidth(fromJson.getPreview().getPhoto().getPhotoWidth());
            }

            return fromJson;
        }

        return null;
    }
}
