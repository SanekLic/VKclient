package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.gson.GsonHelper;

import java.lang.reflect.Type;

public class CropPhotoGsonTypeAdapter implements JsonDeserializer<CropPhoto> {

    @Override
    public CropPhoto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CropPhoto fromJson = GsonHelper.getInstance().getGsonWithPhotoTypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            if (fromJson.getPhoto() != null) {
                fromJson.setCropPhotoUrl(fromJson.getPhoto().getPhotoUrl());
                fromJson.setCropPhotoHeight(fromJson.getPhoto().getPhotoHeight());
                fromJson.setCropPhotoWidth(fromJson.getPhoto().getPhotoWidth());
            }

            if (fromJson.getCrop() != null) {
                fromJson.setCropRectX(fromJson.getRect().getX());
                fromJson.setCropRectX2(fromJson.getRect().getX2());
                fromJson.setCropRectY(fromJson.getRect().getY());
                fromJson.setCropRectY2(fromJson.getRect().getY2());
            }

            return fromJson;
        }

        return null;
    }
}
