package com.my.vkclient.gson.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.Link;
import com.my.vkclient.gson.GsonAdapter;

import java.lang.reflect.Type;

public class LinkGsonTypeAdapter implements JsonDeserializer<Link> {

    @Override
    public Link deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Link fromJson = GsonAdapter.getInstance().getGsonWithPhotoRypeAdapter().fromJson(json, typeOfT);

        if (fromJson != null) {
            Link link = new Link();

            link.setUrl(fromJson.getUrl());

            if (fromJson.getPhoto() != null) {
                link.setPhotoUrl(fromJson.getPhoto().getUrl());
                link.setPhotoHeight(fromJson.getPhoto().getHeight());
                link.setPhotoWidth(fromJson.getPhoto().getWidth());
            }

            return link;
        }

        return null;
    }
}
