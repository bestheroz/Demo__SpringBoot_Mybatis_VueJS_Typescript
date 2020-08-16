package com.github.bestheroz.standard.common.util.adapter;

import com.github.bestheroz.standard.common.util.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantDeserializerTypeAdapter implements JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
        return StringUtils.isNumeric(json.getAsString()) ? Instant.ofEpochMilli(json.getAsLong()) : DateUtils.parseOffsetDateTimeAtUTC(json.getAsString()).toInstant();
    }
}
