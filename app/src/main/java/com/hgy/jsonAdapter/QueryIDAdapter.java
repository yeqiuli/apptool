package com.hgy.jsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hgy.bean.GroupIdBean;
import com.hgy.bean.QueryIdBean;

import java.lang.reflect.Type;
import java.util.List;

public class QueryIDAdapter implements JsonDeserializer<List<QueryIdBean.DataDTO>> {
    @Override
    public List<QueryIdBean.DataDTO> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }
        if (!json.isJsonArray()) {
            return null;
        }
        return context.deserialize(json, typeOfT);
    }
}
