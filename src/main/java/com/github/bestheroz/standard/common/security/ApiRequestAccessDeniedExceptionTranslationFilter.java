package com.github.bestheroz.standard.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ApiRequestAccessDeniedExceptionTranslationFilter extends GenericFilterBean {

    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        log.debug("ApiRequestAccessDeniedExceptionTranslationFilter");
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        try {
            log.debug("111111111111111111");
            chain.doFilter(request, response);
        } catch (final IOException ex) {
            log.debug("@@@@22222222222222222");
            throw ex;
        } catch (final Exception ex) {
            // Rethrow the exception when the request is not an API request
            log.debug("22222222222222222");
            if (!request.getRequestURI().startsWith("/api/") && !request.getRequestURI().startsWith("/rt/")) {
                throw ex;
            }

            log.debug("333333333333333333");
            // Try to extract a SpringSecurityException from the stacktrace
            final Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(ex);
            final RuntimeException ase = (AccessDeniedException) this.throwableAnalyzer.getFirstThrowableOfType(
                    AccessDeniedException.class, causeChain);

            // This is not a Spring Security's AccessDeniedException. We do not need to process it here
            if (ase == null) {
                throw ex;
            }

            log.debug("44444444444444444444");

            if (response.isCommitted()) {
                throw new ServletException("Unable to translate AccessDeniedException because the response" +
                        " of this API request is already committed.", ex);
            }

            // The user is not authenticated. Instead of showing a 403 error, we should
            // send a 401 error to the client, indicating that accessing the requested
            // resources requires authentication and the client hasn't been authenticated.
            //
            // Reference: https://httpstatuses.com/
            if (request.getUserPrincipal() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    /**
     * Default implementation of <code>ThrowableAnalyzer</code> which is capable of also
     * unwrapping <code>ServletException</code>s.
     */
    private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
        /**
         * @see ThrowableAnalyzer#initExtractorMap()
         */
        @Override
        protected void initExtractorMap() {
            super.initExtractorMap();

            this.registerExtractor(ServletException.class, throwable -> {
                ThrowableAnalyzer.verifyThrowableHierarchy(throwable,
                        ServletException.class);
                return ((ServletException) throwable).getRootCause();
            });
        }
    }
}
