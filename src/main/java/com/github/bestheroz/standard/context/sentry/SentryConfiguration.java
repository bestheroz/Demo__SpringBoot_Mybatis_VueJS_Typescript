package com.github.bestheroz.standard.context.sentry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@Profile(value = { "dev", "production" })
public class SentryConfiguration {

  @Bean
  public HandlerExceptionResolver sentryExceptionResolver() {
    return new io.sentry.spring.SentryExceptionResolver();
  }
}
