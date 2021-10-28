package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.authenticate.CustomUserDetails;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@UtilityClass
public class AuthenticationUtils {
  public boolean isSigned() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.isAuthenticated()
        && !StringUtils.equals(authentication.getName(), "anonymousUser");
  }

  public boolean isNotSigned() {
    return !isSigned();
  }

  public CustomUserDetails getSignInVO() {
    try {
      if (isNotSigned()) {
        throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
      }
      return ((CustomUserDetails)
          SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    } catch (final NullPointerException e) {
      throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
    } catch (final Throwable e) {
      log.warn(LogUtils.getStackTrace(e));
      throw e;
    }
  }

  public Long getId() {
    return getSignInVO().getId();
  }

  public Long getRoleId() {
    try {
      return Optional.ofNullable(AuthenticationUtils.getSignInVO())
          .map(CustomUserDetails::getRoleId)
          .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST));
    } catch (final NullPointerException e) {
      throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
    } catch (final Throwable e) {
      log.warn(LogUtils.getStackTrace(e));
      throw e;
    }
  }

  public boolean isSuperAdmin() {
    try {
      return getRoleId().equals(1L);
    } catch (final NullPointerException e) {
      throw new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
    } catch (final Throwable e) {
      log.warn(LogUtils.getStackTrace(e));
      throw e;
    }
  }

  public boolean isNotSuperAdmin() {
    return !isSuperAdmin();
  }

  public void signOut() {
    SecurityContextHolder.clearContext();
  }
}
