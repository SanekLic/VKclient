package com.my.vkclient.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Size;

import java.lang.reflect.Type;

public class PhotoGsonTypeAdapter implements JsonDeserializer<Photo> {

    @Override
    public Photo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Photo fromJson = new Gson().fromJson(json, typeOfT);

        if (fromJson != null) {
            Photo photo = new Photo();

            if (fromJson.getSizes() != null) {
                Size maxSize = fromJson.getSizes().get(0);
                for (Size size : fromJson.getSizes()) {
                    if (maxSize.getWidth() < size.getWidth()) {
                        maxSize = size;
                    }
                }

                String url = maxSize.getUrl() != null ? maxSize.getUrl() : maxSize.getSrc();
                photo.setUrl(url);
                photo.setHeight(maxSize.getHeight());
                photo.setWidth(maxSize.getWidth());
            }

            return photo;
        }

        return null;
    }
}
