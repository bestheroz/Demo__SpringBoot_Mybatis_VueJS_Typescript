package com.github.bestheroz.standard.common.util.adapter;

import com.github.bestheroz.standard.common.util.MapperUtils;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class MapDeserializerTypeAdapter implements JsonDeserializer<Map<String, Object>> {
  @Override
  public Map<String, Object> deserialize(
      final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    final Map<String, Object> map = new HashMap<>();
    final JsonObject jsonObject = json.getAsJsonObject();
    jsonObject
        .entrySet()
        .forEach(
            entry -> {
              final String key = entry.getKey();
              final JsonElement value = entry.getValue();
              if (value.isJsonArray()) {
                map.put(key, MapperUtils.getGsonObject().fromJson(value, ArrayList.class));
              } else if (value.isJsonPrimitive()) {
                // System.err.println(value.toString());
                // String 처리
                if (StringUtils.startsWith(value.toString(), "\"")
                    && StringUtils.endsWith(value.toString(), "\"")) {
                  // System.err.println("String");
                  map.put(key, value.getAsString());
                } else if ( // Double
                StringUtils.countMatches(value.toString(), ".") == 1
                    || StringUtils.countMatches(value.toString(), ".") == 1
                        && StringUtils.countMatches(value.toString(), "E") == 1) {
                  // System.err.println("Double");
                  map.put(key, value.getAsDouble());
                } else if (StringUtils.isNumeric(value.toString())) { // Long
                  // System.err.println("Long");
                  map.put(key, value.getAsLong());
                } else if (value.getAsJsonPrimitive().isBoolean()) { // Boolean
                  // System.err.println("Boolean");
                  map.put(key, value.getAsBoolean());
                } else if (value.getAsJsonPrimitive().isJsonNull()) { // null
                  // System.err.println("null");
                  map.put(key, null);
                } else { // else: String
                  // System.err.println("else String");
                  map.put(key, value.getAsString());
                }
              } else if (value.isJsonObject()) {
                map.put(key, MapperUtils.getGsonObject().fromJson(value, HashMap.class));
              }
            });
    return map;
  }
}
