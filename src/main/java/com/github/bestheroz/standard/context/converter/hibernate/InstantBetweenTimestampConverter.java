package com.github.bestheroz.standard.context.converter.hibernate;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter(autoApply = true)
@Slf4j
public class InstantBetweenTimestampConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(final Instant locDate) {
        return locDate == null ? null : Timestamp.from(locDate);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp sqlDate) {
        return sqlDate == null ? null : sqlDate.toInstant();
    }
}
