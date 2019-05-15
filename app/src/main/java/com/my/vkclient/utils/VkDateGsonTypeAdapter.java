package com.my.vkclient.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.my.vkclient.entities.VkDate;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class VkDateGsonTypeAdapter implements JsonDeserializer<VkDate> {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault());

    @Override
    public VkDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();

        long dateLong = Long.valueOf(dateString) * 1000;

        return new VkDate(simpleDateFormat.format(new java.util.Date(dateLong)));
    }
}
