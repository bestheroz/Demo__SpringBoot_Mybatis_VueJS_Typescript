package com.github.bestheroz.standard.context.converter.hibernate;

import com.github.bestheroz.standard.common.util.DateUtils;
import org.joda.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(final LocalDateTime locDate) {
        return locDate == null ? null : Timestamp.valueOf(locDate.toString(DateUtils.YYYY_MM_DD_HH_MM_SS));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(final Timestamp sqlDate) {
        return DateUtils.getLocalDateTimeIgnoreException(String.valueOf(sqlDate));
    }
}
