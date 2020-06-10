package com.github.bestheroz.standard.common.security;

import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        log.debug("Access to `" + request.getRequestURI() + "` denied.");

        if (request.getRequestURI().startsWith("/api/")) {
            if (request.getUserPrincipal() instanceof TableMemberVO) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.sendRedirect("/login");
        }
    }
}
