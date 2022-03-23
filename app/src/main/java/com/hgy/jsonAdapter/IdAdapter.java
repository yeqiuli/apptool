package com.hgy.jsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hgy.bean.GroupIdBean;

import java.lang.reflect.Type;

public class IdAdapter implements JsonDeserializer<GroupIdBean.DataDTO> {
    @Override
    public GroupIdBean.DataDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }
        if (!json.isJsonObject()) {
            return null;
        }
        return context.deserialize(json, typeOfT);
    }
}
