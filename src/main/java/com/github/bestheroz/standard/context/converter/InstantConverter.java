package com.github.bestheroz.standard.context.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(final String arg0) {
        return Instant.ofEpochMilli(Long.parseLong(arg0));
    }
}
