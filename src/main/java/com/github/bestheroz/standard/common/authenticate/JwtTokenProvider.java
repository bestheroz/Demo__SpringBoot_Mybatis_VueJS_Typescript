package com.github.bestheroz.standard.common.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.auth.AuthService;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.AccessBeanUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.OffsetDateTime;

@Slf4j
@UtilityClass
public class JwtTokenProvider {
    private final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
    private final Long expiresAtAccessToken = 300L;
    private final Long expiresAtRefreshToken = 86400L;

    public String createAccessToken(final TableMemberVO userVO) {
        return getJWTCreator(userVO).withExpiresAt(Date.from(OffsetDateTime.now().plusSeconds(expiresAtAccessToken.intValue()).toInstant())).sign(ALGORITHM);
    }

    public String createRefreshToken(final TableMemberVO userVO, final String accessToken) {
        return getJWTCreator(userVO).withClaim("accessToken", accessToken)
                .withExpiresAt(Date.from(OffsetDateTime.now().plusSeconds(expiresAtRefreshToken.intValue()).toInstant()))
                .sign(ALGORITHM);
    }

    private JWTCreator.Builder getJWTCreator(final TableMemberVO userVO) {
        Assert.notNull(userVO, "userVO parameter must not be empty or null");
        Assert.hasText(userVO.getId(), "userPk parameter must not be empty or null");
        Assert.hasText(userVO.getName(), "userName parameter must not be empty or null");
        Assert.notNull(userVO.getAuthority(), "userAuthority parameter must not be empty or null");
        return JWT.create().withClaim("userPk", userVO.getId()).withClaim("userName", userVO.getName()).withClaim("userAuthority", userVO.getAuthority());
    }

    public Authentication getAuthentication(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        final UserDetails userDetails = AccessBeanUtils.getBean(AuthService.class).getUserDetails(getUserPk(token), getUserName(token), getUserAuthority(token));
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

    public String getUserName(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(expiresAtAccessToken).build().verify(token).getClaims().get("userName").asString();
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
            throw BusinessException.FAIL_TRY_LOGIN_FIRST;
        }
    }

    public Integer getUserAuthority(final String token) {
        Assert.hasText(token, "token parameter must not be empty or null");
        try {
            return JWT.require(ALGORITHM).acceptExpiresAt(expiresAtAccessToken).build().verify(token).getClaims().get("userAuthority").asInt();
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
