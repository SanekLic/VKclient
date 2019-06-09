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
            Doc doc = new Doc();

            doc.setUrl(fromJson.getUrl());

            if (fromJson.getPreview() != null && fromJson.getPreview().getPhoto() != null) {
                doc.setPhotoUrl(fromJson.getPreview().getPhoto().getPhotoUrl());
                doc.setPhotoHeight(fromJson.getPreview().getPhoto().getPhotoHeight());
                doc.setPhotoWidth(fromJson.getPreview().getPhoto().getPhotoWidth());
            }

            return doc;
        }

        return null;
    }
}
