package com.github.bestheroz.standard.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.serializer.InstantDeserializer;
import com.github.bestheroz.standard.common.util.serializer.InstantSerializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.ExceptionUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class MapperUtils {
    private ObjectMapper OBJECT_MAPPER = null;

    public <T> T toObject(final Object content, final Class<T> returnType) {
        try {
            return getObjectMapper().readValue(toByte(content), returnType);
        } catch (final IOException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public Map<String, Object> toMap(final Object content) {
        try {
            return content instanceof Map ? (Map) content : getObjectMapper().readValue(toByte(content), new TypeReference<Map<String, Object>>() {
            });
        } catch (final IOException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public String toString(final Object content) {
        try {
            return content instanceof String ? (String) content : getObjectMapper().writeValueAsString(content);
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public byte[] toByte(final Object content) {
        try {
            return content instanceof byte[] ? (byte[]) content : getObjectMapper().writeValueAsBytes(content);
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public JsonNode toJsonNode(final Object content) {
        try {
            return content instanceof JsonNode ? (JsonNode) content : getObjectMapper().readTree(toByte(content));
        } catch (final IOException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public <T> List<T> toArrayList(final Object content, final Class<T> returnType) {
        try {
            return getObjectMapper().readValue(toByte(content), TypeFactory.defaultInstance().constructCollectionType(List.class, returnType));
        } catch (final IOException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER == null) {
            final SimpleModule module = new SimpleModule();
            module.addSerializer(Instant.class, new InstantSerializer());
            module.addDeserializer(Instant.class, new InstantDeserializer());
            OBJECT_MAPPER = new ObjectMapper().registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return OBJECT_MAPPER;
    }

}
