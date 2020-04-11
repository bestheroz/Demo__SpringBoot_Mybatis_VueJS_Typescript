package com.github.bestheroz.standard.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@UtilityClass
public class NullUtils {

    // int 도 함께 커버됨
    public static boolean equals(final Integer n1, final Integer n2) {
        try {
            return n1 != null && n2 != null && n1.intValue() == n2.intValue();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static boolean equals(final Long n1, final Long n2) {
        try {
            return n1 != null && n2 != null && n1.longValue() == n2.longValue();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static boolean exists(final File file) {
        try {
            return file != null && file.exists();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static boolean hasNext(final Iterator<?> iterator) {
        try {
            return iterator != null && iterator.hasNext();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static boolean isEmpty(final JsonElement json) {
        try {
            if (json == null) {
                return true;
            }
            if (json.isJsonObject()) {
                return json.isJsonNull() || json.getAsJsonObject().entrySet().isEmpty();
            } else if (json.isJsonArray()) {
                return json.isJsonNull() || json.getAsJsonArray().size() == 0;
            } else {
                return json.isJsonNull();
            }
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return true;
        }
    }

    public static boolean isEmpty(final MultipartFile multipartFile) {
        try {
            return multipartFile == null || multipartFile.isEmpty();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return true;
        }
    }

    public static boolean isEmpty(final List<?> list) {
        try {
            return size(list) == 0;
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return true;
        }
    }

    public static boolean isEmpty(final Set<?> list) {
        try {
            return size(list) == 0;
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return true;
        }
    }

    public static boolean isNotEmpty(final JsonElement json) {
        return !isEmpty(json);
    }

    public static boolean isNotEmpty(final MultipartFile multipartFile) {
        return !isEmpty(multipartFile);
    }

    public static boolean isNotEmpty(final List<?> list) {
        return !isEmpty(list);
    }

    public static boolean isNotEmpty(final Set<?> list) {
        return !isEmpty(list);
    }

    public static int size(final List<?> list) {
        try {
            return list == null ? 0 : list.size();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    public static int size(final Set<?> list) {
        try {
            return list == null ? 0 : list.size();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    public static int size(final JsonArray list) {
        try {
            return list == null ? 0 : list.size();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    public static int size(final Map<String, ?> map) {
        try {
            return map == null ? 0 : map.size();
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

}
