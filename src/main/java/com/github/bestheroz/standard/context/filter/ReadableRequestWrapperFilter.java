package com.github.bestheroz.standard.context.filter;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
public class ReadableRequestWrapperFilter extends GenericFilter {
    private static final long serialVersionUID = -3445941001006739628L;

    @Override
    public void init(final FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final ReadableRequestWrapper wrapper = new ReadableRequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrapper, response);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
