package com.my.vkclient.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Video;

import java.lang.reflect.Type;

public class VideoGsonTypeAdapter implements JsonDeserializer<Video> {

    @Override
    public Video deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Video fromJson = new Gson().fromJson(json, typeOfT);

        if (fromJson != null) {
            Video video = new Video();

            String url = fromJson.getPhoto320Url();
            int width = 320;
            int height = 240;

            if (fromJson.getPhoto800Url() != null) {
                url = video.getPhoto800Url();
                width = 800;
                height = 450;
            } else if (fromJson.getPhoto640Url() != null) {
                url = video.getPhoto640Url();
                width = 640;
                height = 480;
            }

            video.setUrl(url);
            video.setHeight(height);
            video.setWidth(width);

            return video;
        }

        return null;
    }
}
