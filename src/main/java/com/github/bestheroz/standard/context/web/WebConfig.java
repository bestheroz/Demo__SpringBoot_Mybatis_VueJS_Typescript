package com.github.bestheroz.standard.context.web;

import com.github.bestheroz.standard.common.interceptor.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("**/swagger-resources/**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html/swagger-resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("index.html").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**").excludePathPatterns("/").excludePathPatterns("/fonts/**").excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**").excludePathPatterns("/img/**").excludePathPatterns("/js/**").excludePathPatterns("/**/*.css").excludePathPatterns("/**/*.ico")
                .excludePathPatterns("/**/*.js").excludePathPatterns("/**/*.json").excludePathPatterns("/**/*.html").excludePathPatterns("/**/*.map").excludePathPatterns("/csrf")
                .excludePathPatterns("/sample/auth/**").excludePathPatterns("/error").excludePathPatterns("/common/exception/error").excludePathPatterns("/swagger");
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/swagger").setViewName("redirect:/swagger-ui.html");
    }

}
