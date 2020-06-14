package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.typeadapter.*;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.lang.reflect.Type;
import java.util.*;

@Slf4j
@UtilityClass
public class MapperUtils {
    private final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializerTypeAdapter()).registerTypeAdapter(Date.class, new DateSerializerTypeAdapter())
            .registerTypeAdapter(DateTime.class, new DateTimeDeserializerTypeAdapter()).registerTypeAdapter(DateTime.class, new DateTimeSerializerTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerTypeAdapter()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializerTypeAdapter())
            .registerTypeAdapter(Map.class, new MapDeserializerTypeAdapter()).registerTypeAdapter(HashMap.class, new MapDeserializerTypeAdapter())
            .registerTypeAdapter(LinkedTreeMap.class, new MapDeserializerTypeAdapter()).disableHtmlEscaping().create();


    public <T> T toObject(final Object content, final Class<T> returnType) {
        return GSON_INSTANCE.fromJson(toJsonElement(content), returnType);
    }

    public <T> T toObject(final Object content, final Type typeOfT) {
        return GSON_INSTANCE.fromJson(toJsonElement(content), typeOfT);
    }

    public JsonArray toJsonArray(final Object content) {
        return getCollectionTypeCatchException(content, JsonArray.class);
    }

    public JsonObject toJsonObject(final Object content) {
        return getCollectionTypeCatchException(content, JsonObject.class);
    }

    public Map<String, Object> toHashMap(final Object content) {
        return toObject(getCollectionTypeCatchException(content, JsonObject.class), new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public JsonPrimitive toJsonPrimitive(final Object content) {
        return toJsonElement(content).getAsJsonPrimitive();
    }

    public JsonElement toJsonElement(final Object content) {
        if (content instanceof String) {
            try {
                return JsonParser.parseString((String) content);
            } catch (final Throwable e) {
                // ignored
                return GSON_INSTANCE.toJsonTree(content);
            }
        } else {
            return GSON_INSTANCE.toJsonTree(content);
        }
    }

    public String toString(final Object content) {
        return GSON_INSTANCE.toJson(content);
    }

    public <T> List<T> toArrayList(final Object content, final Class<T> returnType) {
        final JsonArray array = MapperUtils.toObject(content, JsonArray.class);
        final List<T> lst = new ArrayList<>();
        for (final JsonElement json : array) {
            lst.add(MapperUtils.toObject(json, returnType));
        }
        return lst;
        // return GSON_INSTANCE.fromJson(getJsonElementWithExceptionString(content), new TypeToken<ArrayList<T>>() {
        // }.getType());
    }

    public Gson getGsonObject() {
        return GSON_INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private <T> T getCollectionTypeCatchException(final Object content, final Class<T> returnType) {
        final JsonElement jsonElement = toJsonElement(content);
        if (jsonElement.isJsonPrimitive()) {
            log.warn(jsonElement.toString());
            log.warn(ExceptionCode.ERROR_TRANSFORM_DATA.toString());
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        } else if (jsonElement.isJsonNull()) {
            if (returnType == JsonObject.class) {
                return (T) new JsonObject();
            } else if (returnType == JsonArray.class) {
                return (T) new JsonArray();
            } else if (returnType == Map.class) {
                return (T) new HashMap<String, Object>();
            }
        }

        return toObject(jsonElement, returnType);
    }
}
