package com.github.bestheroz.standard.common.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.auth.AuthService;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
    @Resource private AuthService authService;

    public String createToken(final String userPk) {
        return JWT.create().withClaim("userPk", userPk).withExpiresAt(LocalDateTime.now().plusDays(1).toDate()).sign(ALGORITHM);
    }

    public Authentication getAuthentication(final String token) {
        final UserDetails userDetails = this.authService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(final String token) {
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(86400).build().verify(token).getClaims().get("userPk").asString();
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
    }

    public String resolveToken(final HttpServletRequest request) {
//        return request.getHeader("X-AUTH-TOKEN");
        return request.getHeader("Authorization");
    }

    public boolean validateToken(@NotNull final String token) {
        try {
            JWT.require(ALGORITHM).acceptExpiresAt(86400).build().verify(token);
            return true;
        } catch (final JWTVerificationException | NullPointerException e) {
            return false;
        }
    }

}
