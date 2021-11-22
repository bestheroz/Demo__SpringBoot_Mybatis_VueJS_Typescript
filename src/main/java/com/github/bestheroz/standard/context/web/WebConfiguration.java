package com.github.bestheroz.standard.context.web;

import com.github.bestheroz.standard.common.util.MapperUtils;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/**/*")
        .addResourceLocations("classpath:/static/")
        .resourceChain(true)
        .addResolver(
            new PathResourceResolver() {
              @Override
              protected Resource getResource(final String resourcePath, final Resource location)
                  throws IOException {
                final Resource requestedResource = location.createRelative(resourcePath);
                return requestedResource.exists() && requestedResource.isReadable()
                    ? requestedResource
                    : new ClassPathResource("/static/index.html");
              }
            });
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
