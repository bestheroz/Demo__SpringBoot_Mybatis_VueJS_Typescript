package com.github.bestheroz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.github.bestheroz", exclude = {JacksonAutoConfiguration.class})
@EnableCaching
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
