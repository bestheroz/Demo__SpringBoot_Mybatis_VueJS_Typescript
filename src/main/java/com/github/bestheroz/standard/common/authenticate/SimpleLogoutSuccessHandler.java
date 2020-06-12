package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.standard.common.response.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
            throws IOException {

        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(ApiResult.ok().toString());
    }
}
