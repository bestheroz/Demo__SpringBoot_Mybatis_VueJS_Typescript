package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.util.MapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        final ApiResult failure;
        if (exception instanceof BadCredentialsException) {
            failure = ApiResult.code(ExceptionCode.FAIL_INVALID_CREDENTIALS);
        } else if (exception instanceof InsufficientAuthenticationException) {
            failure = ApiResult.code(ExceptionCode.FAIL_INVALID_AUTHENTICATION_REQUEST);
        } else {
            failure = ApiResult.code(ExceptionCode.FAIL_INVALID_AUTHENTICATION_FAILURE);
        }
        response.getWriter().write(MapperUtils.toString(failure));
    }
}
