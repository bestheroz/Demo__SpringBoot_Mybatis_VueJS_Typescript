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
    private final Long expiresAtAccessToken = 300L;
    private final Long expiresAtRefreshToken = 86400L;

    public String createAccessToken(final String userPk) {
        Assert.hasText(userPk, "userPk parameter must not be empty or null");
        return JWT.create().withClaim("userPk", userPk).withExpiresAt(LocalDateTime.now().plusSeconds(expiresAtAccessToken.intValue()).toDate()).sign(ALGORITHM);
    }

    public String createRefreshToken(final String userPk, final String accessToken) {
        Assert.hasText(userPk, "userPk parameter must not be empty or null");
        return JWT.create().withClaim("userPk", userPk).withClaim("accessToken", accessToken).withExpiresAt(LocalDateTime.now().plusSeconds(expiresAtRefreshToken.intValue()).toDate()).sign(ALGORITHM);
    }

    public Authentication getAuthentication(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        final UserDetails userDetails = AccessBeanUtils.getBean(AuthService.class).loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
    }

    public String getUserPk(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(expiresAtAccessToken).build().verify(token).getClaims().get("userPk").asString();
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
            throw BusinessException.FAIL_TRY_LOGIN_FIRST;
        }
    }

    public String getAccessTokenFromRefreshToken(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(expiresAtRefreshToken).build().verify(token).getClaims().get("accessToken").asString();
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
            throw BusinessException.FAIL_TRY_LOGIN_FIRST;
        }
    }

    public String resolveAccessToken(final HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        return StringUtils.equals(authorization, "null") ? null : authorization;
    }

    public String resolveRefreshToken(final HttpServletRequest request) {
        final String authorizationR = request.getHeader("AuthorizationR");
        return StringUtils.equals(authorizationR, "null") ? null : authorizationR;
    }

    public boolean validateAccessToken(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            JWT.require(ALGORITHM).acceptExpiresAt(expiresAtAccessToken).build().verify(token);
            return true;
        } catch (final JWTVerificationException | NullPointerException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(final String token, final String refreshToken) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            JWT.require(ALGORITHM).acceptExpiresAt(expiresAtRefreshToken).build().verify(refreshToken);
            return StringUtils.equals(getAccessTokenFromRefreshToken(refreshToken), token);
        } catch (final JWTVerificationException | NullPointerException e) {
            return false;
        }
    }
}
