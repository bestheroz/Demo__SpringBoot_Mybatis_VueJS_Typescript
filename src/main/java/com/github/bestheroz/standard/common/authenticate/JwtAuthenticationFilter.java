package com.github.bestheroz.standard.common.authenticate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final String token = JwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        if (token != null && JwtTokenProvider.validateToken(token)) {
            SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(token));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
