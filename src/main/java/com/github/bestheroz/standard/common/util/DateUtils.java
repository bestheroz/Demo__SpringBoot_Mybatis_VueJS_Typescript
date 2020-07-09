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
    public final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public final DateTimeFormatter ISO_8601 = DateTimeFormatter.ISO_INSTANT;

    public final DateTimeFormatter HHMMSS = DateTimeFormatter.ofPattern("HHmmss");
    public final DateTimeFormatter YYYYMM = DateTimeFormatter.ofPattern("yyyyMM");
    public final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public String getStringNow(final String pattern) {
        return OffsetDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getStringNow(final DateTimeFormatter formatter) {
        return OffsetDateTime.now().format(formatter);
    }

    public String getString(final Instant instant, final String pattern) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getString(final Instant instant, final DateTimeFormatter formatter) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).format(formatter);
    }

    public String getString(final Long timestamp, final String pattern) {
        if (timestamp == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getString(final Long timestamp, final DateTimeFormatter formatter) {
        if (timestamp == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(formatter);
    }

    public String getString(final Date date, final String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return DateUtils.getString(date.getTime(), pattern);
    }

    public String getString(final Date date, final DateTimeFormatter formatter) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return DateUtils.getString(date.getTime(), formatter);
    }

    public String getString(final String string, final String fromPattern, final String toPattern) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return Objects.requireNonNull(getOffsetDateTime(string, fromPattern)).format(DateTimeFormatter.ofPattern(toPattern));
    }

    public String getString(final String string, final DateTimeFormatter fromFormatter, final DateTimeFormatter toFormatter) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return Objects.requireNonNull(getOffsetDateTime(string, fromFormatter)).format(toFormatter);
    }

    public OffsetDateTime getOffsetDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return OffsetDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    public OffsetDateTime getOffsetDateTime(final String text, final DateTimeFormatter fromFormatter) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return OffsetDateTime.parse(text, fromFormatter);
    }


//    public OffsetDateTime getOffsetDateTimeIgnoreException(final String arg0) {
//        if (StringUtils.isNotEmpty(arg0)) {
//            try {
//                // 1. long값(timestamp)
//                return getDateTime(Long.parseLong(arg0));
//            } catch (final Throwable e) {
//                try {
//                    // 2. yyyy-MM-dd
//                    return getDateTime(arg0, DateUtils.YYYY_MM_DD);
//                } catch (final Throwable e2) {
//                    try {
//                        // 3. yyyy-MM-dd HH:mm:ss
//                        return getDateTime(arg0, DateUtils.YYYY_MM_DD_HH_MM_SS);
//                    } catch (final Throwable e3) {
//                        try {
//                            // 4. yyyy-MM-dd HH:mm:ss.SSS
//                            return getDateTime(arg0, "yyyy-MM-dd HH:mm:ss.SSS");
//                        } catch (final Throwable e4) {
//                            try {
//                                // 5. yyyy-MM-ddTHH:mm:ss.SSSZ
//                                return getDateTime(arg0, DateUtils.ISO_8601);
//                            } catch (final Throwable e5) {
//                                try {
//                                    // 6. yyyyMMdd
//                                    return getDateTime(arg0, DateUtils.YYYYMMDD);
//                                } catch (final Throwable e6) {
//                                    try {
//                                        // 7. yyyyMMddHHmmss
//                                        return getDateTime(arg0, DateUtils.YYYYMMDDHHMMSS);
//                                    } catch (final Throwable e7) {
//                                        return null;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else {
//            return null;
//        }
//    }
}
