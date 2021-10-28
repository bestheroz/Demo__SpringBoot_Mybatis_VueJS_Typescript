package com.github.bestheroz.standard.common.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.LogUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.sql.Date;
import java.time.OffsetDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

@Slf4j
@UtilityClass
public class JwtTokenProvider {
  private final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
  private final Long expiresAtAccessToken = 300L;
  private final Long expiresAtRefreshToken = 2592000L; // 3600 * 24 * 30 == 1month

  public String createAccessToken(final CustomUserDetails customUserDetails) {
    Assert.notNull(customUserDetails, "customUserDetails parameter must not be empty or null");
    Assert.hasText(customUserDetails.getAdminId(), "adminId parameter must not be empty or null");
    Assert.hasText(customUserDetails.getName(), "name parameter must not be empty or null");
    Assert.notNull(customUserDetails.getRoleId(), "roleId parameter must not be empty or null");
    return JWT.create()
        .withClaim("id", customUserDetails.getId())
        .withClaim("admin", MapperUtils.toString(customUserDetails))
        .withExpiresAt(
            Date.from(
                OffsetDateTime.now().plusSeconds(expiresAtAccessToken.intValue()).toInstant()))
        .sign(ALGORITHM);
  }

  public String createRefreshToken(final CustomUserDetails customUserDetails) {
    Assert.notNull(customUserDetails, "customUserDetails parameter must not be empty or null");
    Assert.hasText(customUserDetails.getAdminId(), "adminId parameter must not be empty or null");
    return JWT.create()
        .withClaim("id", customUserDetails.getId())
        .withExpiresAt(
            Date.from(
                OffsetDateTime.now().plusSeconds(expiresAtRefreshToken.intValue()).toInstant()))
        .sign(ALGORITHM);
  }

  public Authentication getAuthentication(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    final UserDetails userDetails = getCustomUserDetails(token);
    return new UsernamePasswordAuthenticationToken(
        userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
  }

  public Long getId(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    try {
      return JWT.require(ALGORITHM)
          .acceptExpiresAt(expiresAtAccessToken)
          .build()
          .verify(token)
          .getClaims()
          .get("id")
          .asLong();
    } catch (final JWTVerificationException | NullPointerException e) {
      throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
    }
  }

  public CustomUserDetails getCustomUserDetails(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    try {
      return MapperUtils.toObject(
          JWT.require(ALGORITHM)
              .acceptExpiresAt(expiresAtAccessToken)
              .build()
              .verify(token)
              .getClaims()
              .get("admin")
              .asString(),
          CustomUserDetails.class);
    } catch (final JWTVerificationException | NullPointerException e) {
      log.warn(LogUtils.getStackTrace(e));
      throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
    }
  }

  public String resolveAccessToken(final HttpServletRequest request) {
    final String authorization = request.getHeader("Authorization");
    return StringUtils.equals(authorization, "null") ? null : authorization;
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
}
