package com.github.bestheroz.standard.context.converter;

import java.time.Instant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InstantConverter implements Converter<String, Instant> {

  @Override
  public Instant convert(final String arg0) {
    return Instant.parse(arg0);
  }
}
