package com.github.bestheroz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.github.bestheroz")
@EnableCaching
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
