package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.standard.context.security.SecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.tika.utils.ExceptionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP = "[{}]{} ... Request Complete Execute Time : {}";
    private static final String REQUEST_PARAMETERS = "<{}>{}";

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) {
        final String requestURI = new UrlPathHelper().getPathWithinApplication(request);
        log.info(REQUEST_PARAMETERS, request.getMethod(), requestURI);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Arrays.stream(SecurityConfiguration.PUBLIC).map(item -> item.replace("*", "")).filter(requestURI::startsWith).findFirst().ifPresentOrElse(item -> {
            try {
                chain.doFilter(request, response);
                stopWatch.stop();
                log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, response.getStatus(), requestURI, stopWatch.toString());
            } catch (final IOException | ServletException e) {
                this.logger.warn(ExceptionUtils.getStackTrace(e));
            }
        }, () -> {
            try {
                final String token = JwtTokenProvider.resolveAccessToken(request);
                if (StringUtils.isEmpty(token)) {
                    log.info("non accessToken");
                    (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    stopWatch.stop();
                    log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, response.getStatus(), requestURI, stopWatch.toString());
                    return;
                }
                if (!JwtTokenProvider.validateAccessToken(token)) {
                    log.info("invalid accessToken - refreshToken must ");
                    (response).addHeader("refreshToken", "must");
                    (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    stopWatch.stop();
                    log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, response.getStatus(), requestURI, stopWatch.toString());
                    return;
                }

                try {
                    SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(token));
                } catch (final UsernameNotFoundException e) {
                    log.info("invalid accessToken");
                    (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    stopWatch.stop();
                    log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, response.getStatus(), requestURI, stopWatch.toString());
                }
                chain.doFilter(request, response);
                stopWatch.stop();
                log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, response.getStatus(), requestURI, stopWatch.toString());
            } catch (final IOException | ServletException e) {
                this.logger.warn(ExceptionUtils.getStackTrace(e));
            }
        });
    }
}
