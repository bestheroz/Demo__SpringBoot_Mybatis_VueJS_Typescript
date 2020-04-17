package com.github.bestheroz.standard.context.web;

import com.github.bestheroz.standard.common.interceptor.Interceptor;
import com.github.bestheroz.standard.common.util.MapperUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**")
                .excludePathPatterns("/", "/fonts/**", "/webjars/**", "/img/**", "/js/**", "/**/*.css", "/**/*.ico", "/**/*.js", "/**/*.json", "/**/*.html", "/**/*.map", "/csrf", "/auth/login",
                        "/error", "/common/exception/error", "/variable/**", "/actuator/**", "/external/**");
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.clear();

        final GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(MapperUtils.getGsonObject());
        converters.add(gsonHttpMessageConverter);

        WebMvcConfigurer.super.configureMessageConverters(converters);
    }
}
