package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.gson.GsonAdapter;

import java.lang.reflect.Type;

public class CropPhotoGsonTypeAdapter implements JsonDeserializer<CropPhoto> {

    @Override
    public CropPhoto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CropPhoto fromJson = GsonAdapter.getInstance().getGsonWithPhotoRypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            CropPhoto cropPhoto = new CropPhoto();
            if (fromJson.getPhoto() != null) {

                cropPhoto.setCropPhotoUrl(fromJson.getPhoto().getPhotoUrl());
                cropPhoto.setCropPhotoHeight(fromJson.getPhoto().getPhotoHeight());
                cropPhoto.setCropPhotoWidth(fromJson.getPhoto().getPhotoWidth());
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
