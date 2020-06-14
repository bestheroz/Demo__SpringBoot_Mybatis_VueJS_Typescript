package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthenticationUtils {

    protected AuthenticationUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isLoggedIn() {
        try {
            getLoginVO();
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    public static boolean isNotLoggedIn() {
        return !isLoggedIn();
    }

    public static UserVO getLoginVO() {
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.isAuthenticated() || StringUtils.equals(authentication.getName(), "anonymousUser")) {
                log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
                throw BusinessException.FAIL_TRY_LOGIN_FIRST;
            }
            return ((UserVO) authentication.getPrincipal());
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public static String getUserName() {
        try {
            final String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (username == null) {
                log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
                throw BusinessException.FAIL_TRY_LOGIN_FIRST;
            }
            return username;
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public static void logout() {
        SecurityContextHolder.clearContext();
    }
}
