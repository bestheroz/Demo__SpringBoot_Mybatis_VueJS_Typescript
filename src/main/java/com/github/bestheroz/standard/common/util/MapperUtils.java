package com.github.bestheroz.standard.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.serializer.InstantSerializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.ExceptionUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class MapperUtils {
    private ObjectMapper OBJECT_MAPPER = null;

    public <T> T toObject(final Object content, final Class<T> returnType) {
        try {
            return getObjectMapper().readValue(toString(content), returnType);
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public <T> T toObject(final Object content, final JavaType typeOfT) {
        try {
            return getObjectMapper().readValue(toString(content), typeOfT);
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public Map<String, Object> toHashMap(final Object content) {
        try {
            return getObjectMapper().readValue(toString(content), new TypeReference<Map<String, Object>>() {
            });
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public String toString(final Object content) {
        try {
            return getObjectMapper().writeValueAsString(content);
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public JsonNode toJsonNode(final Object content) {
        try {
            return getObjectMapper().readTree(toString(content));
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public <T> List<T> toArrayList(final Object content, final Class<T> returnType) {
        try {
            return getObjectMapper().readValue(toString(content), new TypeReference<List<T>>() {
            });
        } catch (final JsonProcessingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
        }
    }

    public ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER == null) {
            final SimpleModule module = new SimpleModule();
            module.addSerializer(Instant.class, new InstantSerializer());
            OBJECT_MAPPER = new ObjectMapper().registerModule(module);
        }
        return OBJECT_MAPPER;
    }

}
