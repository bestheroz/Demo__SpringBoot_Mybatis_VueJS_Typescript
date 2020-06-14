package com.github.bestheroz.standard.common.authenticate;

import org.springframework.security.core.Authentication;
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
        // 헤더에서 JWT 를 받아옵니다.
        final String token = JwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        // 유효한 토큰인지 확인합니다.
        if (token != null && JwtTokenProvider.validateToken(token)) {
//            throw BusinessException.FAIL_TRY_LOGIN_FIRST;
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            final Authentication authentication = JwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
