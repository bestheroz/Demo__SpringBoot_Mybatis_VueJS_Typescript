package com.github.bestheroz.standard.common.security;

import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final static Logger log = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Access to `" + request.getRequestURI() + "` denied.");
        }

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
