package com.my.vkclient.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.CropPhoto;

import java.lang.reflect.Type;

public class CropPhotoGsonTypeAdapter implements JsonDeserializer<CropPhoto> {

    @Override
    public CropPhoto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CropPhoto fromJson = new Gson().fromJson(json, typeOfT);

        if (fromJson != null) {
            CropPhoto cropPhoto = new CropPhoto();
            if (fromJson.getPhoto() != null && fromJson.getPhoto().getSizes() != null) {

                cropPhoto.setCropPhotoUrl(fromJson.getPhoto().getUrl());
                cropPhoto.setCropPhotoHeight(fromJson.getPhoto().getHeight());
                cropPhoto.setCropPhotoWidth(fromJson.getPhoto().getWidth());
            }

            if (fromJson.getCrop() != null) {
                cropPhoto.setCropRectX(fromJson.getRect().getX());
                cropPhoto.setCropRectX2(fromJson.getRect().getX2());
                cropPhoto.setCropRectY(fromJson.getRect().getY());
                cropPhoto.setCropRectY2(fromJson.getRect().getY2());
            }

            return cropPhoto;
        }

        return null;
    }
}
