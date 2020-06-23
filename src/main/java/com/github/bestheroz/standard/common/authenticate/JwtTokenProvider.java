package com.github.bestheroz.standard.common.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.auth.AuthService;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.AccessBeanUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@UtilityClass
public class JwtTokenProvider {
    private final Algorithm ALGORITHM = Algorithm.HMAC512("secret");

    public String createToken(final String userPk) {
        Assert.hasText(userPk, "userPk parameter must not be empty or null");
        return JWT.create().withClaim("userPk", userPk).withExpiresAt(LocalDateTime.now().plusDays(1).toDate()).sign(ALGORITHM);
    }

    public Authentication getAuthentication(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        final UserDetails userDetails = AccessBeanUtils.getBean(AuthService.class).loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
    }

    public String getUserPk(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(86400).build().verify(token).getClaims().get("userPk").asString();
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
            throw BusinessException.FAIL_TRY_LOGIN_FIRST;
        }
    }

    public String resolveToken(final HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            JWT.require(ALGORITHM).acceptExpiresAt(86400).build().verify(token);
            return true;
        } catch (final JWTVerificationException | NullPointerException e) {
            return false;
        }
    }
}
