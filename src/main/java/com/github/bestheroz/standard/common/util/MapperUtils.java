package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.adapter.InstantDeserializerTypeAdapter;
import com.github.bestheroz.standard.common.util.adapter.InstantSerializerTypeAdapter;
import com.github.bestheroz.standard.common.util.adapter.MapDeserializerTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MapperUtils {
  private final Gson GSON_INSTANCE =
      new GsonBuilder()
          .registerTypeAdapter(Instant.class, new InstantDeserializerTypeAdapter())
          .registerTypeAdapter(Instant.class, new InstantSerializerTypeAdapter())
          .registerTypeAdapter(Map.class, new MapDeserializerTypeAdapter())
          .registerTypeAdapter(HashMap.class, new MapDeserializerTypeAdapter())
          .registerTypeAdapter(LinkedTreeMap.class, new MapDeserializerTypeAdapter())
          .disableHtmlEscaping()
          .create();

  public <T> T toObject(final Object source, final Class<T> targetType) {
    return GSON_INSTANCE.fromJson(toJsonElement(source), targetType);
  }

  public <T> T toObject(final Object source, final Type targetType) {
    return GSON_INSTANCE.fromJson(toJsonElement(source), targetType);
  }

  public JsonArray toJsonArray(final Object source) {
    return getCollectionTypeCatchException(source, JsonArray.class);
  }

  public JsonObject toJsonObject(final Object source) {
    return getCollectionTypeCatchException(source, JsonObject.class);
  }

  public Map<String, Object> toMap(final Object source) {
    return toObject(
        getCollectionTypeCatchException(source, JsonObject.class),
        new TypeToken<HashMap<String, Object>>() {}.getType());
  }

  public JsonPrimitive toJsonPrimitive(final Object source) {
    return toJsonElement(source).getAsJsonPrimitive();
  }

  public JsonElement toJsonElement(final Object source) {
    if (source instanceof String str) {
      try {
        return JsonParser.parseString(str);
      } catch (final Throwable e) {
        // ignored
        return GSON_INSTANCE.toJsonTree(source);
      }
    } else {
      return GSON_INSTANCE.toJsonTree(source);
    }
  }

  public String toString(final Object source) {
    if (source instanceof Optional o) {
      if (o.isEmpty()) {
        return "";
      } else {
        return GSON_INSTANCE.toJson(o.get());
      }
    } else {
      return GSON_INSTANCE.toJson(source);
    }
  }

  public <T> List<T> toArrayList(final Object source, final Class<T> targetType) {
    final JsonArray array = MapperUtils.toObject(source, JsonArray.class);
    final List<T> lst = new ArrayList<>();
    for (final JsonElement json : array) {
      lst.add(MapperUtils.toObject(json, targetType));
    }
    return lst;
  }

  public Gson getGsonObject() {
    return GSON_INSTANCE;
  }

  @SuppressWarnings("unchecked")
  private <T> T getCollectionTypeCatchException(final Object source, final Class<T> targetType) {
    final JsonElement jsonElement = toJsonElement(source);
    if (jsonElement.isJsonPrimitive()) {
      log.warn(jsonElement.toString());
      log.warn(ExceptionCode.ERROR_SYSTEM.toString());
      throw BusinessException.ERROR_SYSTEM;
    } else if (jsonElement.isJsonNull()) {
      if (targetType == JsonObject.class) {
        return (T) new JsonObject();
      } else if (targetType == JsonArray.class) {
        return (T) new JsonArray();
      } else if (targetType == Map.class) {
        return (T) new HashMap<String, Object>();
      }
    }

    return toObject(jsonElement, targetType);
  }
}
