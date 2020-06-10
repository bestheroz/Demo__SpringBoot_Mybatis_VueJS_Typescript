package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.standard.common.util.MapperUtils;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/auth/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException, IOException {

        log.debug("Processing login request");

        final String requestBody = IOUtils.toString(request.getReader());
        log.debug("requestBody : {}", requestBody);
        final LoginRequest loginRequest = MapperUtils.toObject(requestBody, LoginRequest.class);
        log.debug(loginRequest.getPassword());
        log.debug("{}", loginRequest.isInvalid());
        if (loginRequest == null || loginRequest.isInvalid()) {
            throw new InsufficientAuthenticationException("Invalid authentication request");
        }

        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.id, loginRequest.password);
        log.debug("token: {}", token);
        final Authentication authenticate = this.getAuthenticationManager().authenticate(token);
        log.debug("authenticate: {}", authenticate);
        return authenticate;
    }

    @Data
    static class LoginRequest {
        private String id;
        private String password;

        public boolean isInvalid() {
            return StringUtils.isAnyBlank(this.id, this.password);
        }
    }
}
