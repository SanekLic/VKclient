package com.my.vkclient.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Size;

import java.lang.reflect.Type;

public class CropPhotoGsonTypeAdapter implements JsonDeserializer<CropPhoto> {

    @Override
    public CropPhoto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CropPhoto fromJson = new Gson().fromJson(json, typeOfT);

        if (fromJson != null) {
            CropPhoto cropPhoto = new CropPhoto();
            if (fromJson.getPhoto() != null && fromJson.getPhoto().getSizes() != null) {
                Size maxSize = fromJson.getPhoto().getSizes().get(0);
                for (Size size : fromJson.getPhoto().getSizes()) {
                    if (maxSize.getWidth() < size.getWidth()) {
                        maxSize = size;
                    }
                }

                String url = maxSize.getUrl() != null ? maxSize.getUrl() : maxSize.getSrc();
                cropPhoto.setCropPhotoUrl(url);
                cropPhoto.setCropPhotoHeight(maxSize.getHeight());
                cropPhoto.setCropPhotoWidth(maxSize.getWidth());
            }

            if (fromJson.getCrop() != null) {
                cropPhoto.setCropRectX(fromJson.getRect().getX());
                cropPhoto.setCropRectX2(fromJson.getRect().getX2());
                cropPhoto.setCropRectY(fromJson.getRect().getY());
                cropPhoto.setCropRectY2(fromJson.getRect().getY2());

//                cropPhoto.setCropRectX(fromJson.getRect().getX() * fromJson.getCrop().getX() / Constants.PERCENTAGE);
//                cropPhoto.setCropRectX2(fromJson.getRect().getX2() * fromJson.getCrop().getX2() / Constants.PERCENTAGE);
//                cropPhoto.setCropRectY(fromJson.getRect().getY() * fromJson.getCrop().getY() / Constants.PERCENTAGE);
//                cropPhoto.setCropRectY2(fromJson.getRect().getY2() * fromJson.getCrop().getY2() / Constants.PERCENTAGE);
            }

            return cropPhoto;
        }

        return null;
    }
}
