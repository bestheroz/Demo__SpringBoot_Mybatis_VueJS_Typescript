package com.github.bestheroz.standard.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.*;

@UtilityClass
public class DateUtils {

    public static final DateTimeZone TIME_ZONE_ASIA_SEOUL = DateTimeZone.forID("Asia/Seoul");
    public static final Locale LOCALE_KOREAN = Locale.KOREAN;

    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String HHMMSS = "HHmmss";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String getStringNow(final String pattern) {
        return new DateTime().toString(pattern);
    }

    public static String getString(final Long timestamp, final String pattern) {
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp).toString(pattern);
    }

    public static String getString(final Date date, final String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return DateUtils.getString(date.getTime(), pattern);
    }

    // public static String getString(final DateTime dateTime, final String pattern) {
    // return dateTime.toString(pattern);
    // }

    public static String getString(final DateTime dateTime, final String pattern) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return dateTime.toString(pattern);
    }

    public static String getString(final String string, final String fromPattern, final String toPattern) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return Objects.requireNonNull(getDateTime(string, fromPattern)).toString(toPattern);
    }

    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    public static LocalDateTime getLocalDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return LocalDateTime.parse(text, DateTimeFormat.forPattern(pattern));
    }

    public static LocalDateTime getLocalDateTime(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new LocalDateTime(timestamp);
    }

    public static LocalDateTime getLocalDateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return new LocalDateTime(date);
    }

    public static List<LocalDateTime> getBeetwenAllLocolDate(final String from, final String to, final String pattern) {
        LocalDateTime fromDt = DateUtils.getLocalDateTime(from, pattern);
        final LocalDateTime toDt = DateUtils.getLocalDateTime(to, pattern);

        if (fromDt == null || toDt == null) {
            return null;
        }

        final List<LocalDateTime> res = new ArrayList<>();
        while (fromDt.compareTo(toDt) != 1) {
            res.add(fromDt);
            if (YYYYMMDD.equals(pattern)) {
                // 시작날짜 + 1 일
                fromDt = fromDt.plusDays(1);
            } else if (YYYYMM.equals(pattern)) {
                fromDt = fromDt.plusMonths(1);
            }
        }
        return res;
    }

    public static DateTime getDateTimeNow() {
        return DateTime.now(DateTimeZone.getDefault());
    }

    public static DateTime getDateTime(final String text, final String pattern) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return DateTime.parse(text, DateTimeFormat.forPattern(pattern));
    }

    public static DateTime getDateTime(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp, DateTimeZone.getDefault());
    }

    public static DateTime getDateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date, DateTimeZone.getDefault());
    }

    public static List<DateTime> getBeetwenAllDate(final String from, final String to, final String pattern) {
        DateTime fromDt = DateUtils.getDateTime(from, pattern);
        final DateTime toDt = DateUtils.getDateTime(to, pattern);

        if (fromDt == null || toDt == null) {
            return null;
        }

        final List<DateTime> res = new ArrayList<>();
        while (fromDt.compareTo(toDt) != 1) {
            res.add(fromDt);
            if (YYYYMMDD.equals(pattern)) {
                // 시작날짜 + 1 일
                fromDt = fromDt.plusDays(1);
            } else if (YYYYMM.equals(pattern)) {
                fromDt = fromDt.plusMonths(1);
            }
        }
        return res;
    }

    public static LocalDateTime getLocalDateTimeIgnoreException(final String arg0) {
        final DateTime dateTimeIgnoreException = getDateTimeIgnoreException(arg0);
        return dateTimeIgnoreException == null ? null : dateTimeIgnoreException.toLocalDateTime();
    }

    public static DateTime getDateTimeIgnoreException(final String arg0) {
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
