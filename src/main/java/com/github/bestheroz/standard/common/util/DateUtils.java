package com.github.bestheroz.standard.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@UtilityClass
public class DateUtils {
    public final String HH_MM_SS = "HH:mm:ss";
    public final String YYYY_MM_DD = "yyyy-MM-dd";
    public final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public final String HHMMSS = "HHmmss";
    public final String YYYYMM = "yyyyMM";
    public final String YYYYMMDD = "yyyyMMdd";
    public final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public String getStringNow(final String pattern) {
        return new DateTime().toString(pattern);
    }

    public String getString(final Long timestamp, final String pattern) {
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp).toString(pattern);
    }

    public String getString(final Date date, final String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return DateUtils.getString(date.getTime(), pattern);
    }

    public String getString(final String string, final String fromPattern, final String toPattern) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return Objects.requireNonNull(getDateTime(string, fromPattern)).toString(toPattern);
    }

    public LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return LocalDateTime.parse(text, DateTimeFormat.forPattern(pattern));
    }

    public LocalDateTime getLocalDateTime(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new LocalDateTime(timestamp);
    }

    public LocalDateTime getLocalDateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return new LocalDateTime(date);
    }

    public DateTime getDateTimeNow() {
        return DateTime.now(DateTimeZone.getDefault());
    }

    public DateTime getDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return DateTime.parse(text, DateTimeFormat.forPattern(pattern));
    }

    public DateTime getDateTime(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp, DateTimeZone.getDefault());
    }

    public DateTime getDateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date, DateTimeZone.getDefault());
    }

    public LocalDateTime getLocalDateTimeIgnoreException(final String arg0) {
        final DateTime dateTimeIgnoreException = getDateTimeIgnoreException(arg0);
        return dateTimeIgnoreException == null ? null : dateTimeIgnoreException.toLocalDateTime();
    }

    public DateTime getDateTimeIgnoreException(final String arg0) {
        if (StringUtils.isNotEmpty(arg0)) {
            try {
                // 1. long값(timestamp)
                return getDateTime(Long.parseLong(arg0));
            } catch (final Throwable e) {
                try {
                    // 2. yyyy-MM-dd
                    return getDateTime(arg0, DateUtils.YYYY_MM_DD);
                } catch (final Throwable e2) {
                    try {
                        // 3. yyyy-MM-dd HH:mm:ss
                        return getDateTime(arg0, DateUtils.YYYY_MM_DD_HH_MM_SS);
                    } catch (final Throwable e3) {
                        try {
                            // 4. yyyy-MM-dd HH:mm:ss.SSS
                            return getDateTime(arg0, "yyyy-MM-dd HH:mm:ss.SSS");
                        } catch (final Throwable e4) {
                            try {
                                // 5. yyyy-MM-ddTHH:mm:ss.SSSZ
                                return getDateTime(arg0, DateUtils.ISO_8601);
                            } catch (final Throwable e5) {
                                try {
                                    // 6. yyyyMMdd
                                    return getDateTime(arg0, DateUtils.YYYYMMDD);
                                } catch (final Throwable e6) {
                                    try {
                                        // 7. yyyyMMddHHmmss
                                        return getDateTime(arg0, DateUtils.YYYYMMDDHHMMSS);
                                    } catch (final Throwable e7) {
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return null;
        }
    }

}
