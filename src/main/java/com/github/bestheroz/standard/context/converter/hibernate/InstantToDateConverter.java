package com.github.bestheroz.standard.context.converter.hibernate;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter(autoApply = true)
@Slf4j
public class InstantToDateConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(final Instant locDate) {
        log.debug("OffsetDateTimeConverter");
        log.debug("{}", locDate);
        return locDate == null ? null : Timestamp.from(locDate);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp sqlDate) {
        return sqlDate.toInstant();
    }
}
