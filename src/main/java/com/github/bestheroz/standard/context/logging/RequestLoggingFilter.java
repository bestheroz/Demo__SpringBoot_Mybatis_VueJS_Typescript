package com.github.bestheroz.standard.context.logging;

import com.github.bestheroz.standard.context.filter.ReadableRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {
    private static final String REQUEST_PARAMETERS = "<{}>{}, parameters={}";

    @Override
    protected void beforeRequest(final HttpServletRequest request, final String message) {
        if (StringUtils.equals(request.getMethod(), "GET")) {
            log.info(REQUEST_PARAMETERS, request.getMethod(), new UrlPathHelper().getPathWithinApplication(request), "");
        } else {
            try {
                log.info(REQUEST_PARAMETERS, request.getMethod(), new UrlPathHelper().getPathWithinApplication(request), IOUtils.toString(((ReadableRequestWrapper) request).getInputStream(),
                        StandardCharsets.UTF_8));
            } catch (final IOException e) {
                log.warn(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {
        // super.afterRequest(request, message);
    }
}
