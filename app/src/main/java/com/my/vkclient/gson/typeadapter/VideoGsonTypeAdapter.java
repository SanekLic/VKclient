package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Video;
import com.my.vkclient.gson.GsonHelper;

import java.lang.reflect.Type;

public class VideoGsonTypeAdapter implements JsonDeserializer<Video> {

    @Override
    public Video deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Video fromJson = GsonHelper.getInstance().getGsonWithOnlyPhotoTypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            String photoUrl = fromJson.getPhoto320Url();
            int width = 320;
            int height = 240;

            if (fromJson.getPhoto800Url() != null) {
                photoUrl = fromJson.getPhoto800Url();
                width = 800;
                height = 450;
            } else if (fromJson.getPhoto640Url() != null) {
                photoUrl = fromJson.getPhoto640Url();
                width = 640;
                height = 480;
            }

            fromJson.setPhotoUrl(photoUrl);
            fromJson.setPhotoHeight(height);
            fromJson.setPhotoWidth(width);

            return fromJson;
        }

        return null;
    }
}
