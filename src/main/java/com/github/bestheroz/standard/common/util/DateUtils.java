package com.github.bestheroz.standard.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@UtilityClass
public class DateUtils {
    public String toStringNow(final String pattern) {
        return OffsetDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String toString(final Instant instant, final String pattern) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String toString(final Long timestamp, final String pattern) {
        if (timestamp == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String toString(final Date date, final String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return DateUtils.toString(date.getTime(), pattern);
    }

    public String toString(final String string, final String fromPattern, final String toPattern) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return Objects.requireNonNull(parseOffsetDateTime(string, fromPattern)).format(DateTimeFormatter.ofPattern(toPattern));
    }

    public OffsetDateTime parseOffsetDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return OffsetDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    public OffsetDateTime parseOffsetDateTimeAtUTC(final String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return OffsetDateTime.parse(text);
    }
}
