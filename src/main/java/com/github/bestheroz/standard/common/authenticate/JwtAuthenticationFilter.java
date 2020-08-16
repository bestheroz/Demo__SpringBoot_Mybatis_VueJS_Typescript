package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.sample.api.entity.member.TableMemberEntity;
import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.util.AccessBeanUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import com.github.bestheroz.standard.context.security.SecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
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
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP = "{} ....... Request Complete Execute Time ....... : {}";
    private static final String REQUEST_PARAMETERS = "<{}>{}";

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final String requestURI = new UrlPathHelper().getPathWithinApplication(request);
        log.info(REQUEST_PARAMETERS, request.getMethod(), requestURI);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final String token = JwtTokenProvider.resolveAccessToken(request);
        final String refreshToken = JwtTokenProvider.resolveRefreshToken(request);
        final Optional<String> oPublicPages = Arrays.stream(SecurityConfiguration.PUBLIC).map(item -> item.replace("*", "")).filter(requestURI::startsWith).findFirst();
        if (oPublicPages.isEmpty()) {
            if (StringUtils.isAnyEmpty(token, refreshToken)) {
                log.debug("non token");
                (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (token != null) {
                if (JwtTokenProvider.validateAccessToken(token)) {
                    try {
                        SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(token));
                    } catch (final UsernameNotFoundException e) {
                        log.debug("invalid token");
                        (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } else if (JwtTokenProvider.validateRefreshToken(token, refreshToken)) {
                    final Optional<TableMemberEntity> oTableMemberEntity = AccessBeanUtils.getBean(TableMemberRepository.class).getItem(TableMemberEntity.class, Map.of("token", refreshToken));
                    if (oTableMemberEntity.isEmpty()) {
                        log.debug("invalid refresh-token");
                        (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    final TableMemberEntity tableMemberEntity = oTableMemberEntity.get();
                    final UserVO userVO = MapperUtils.toObject(tableMemberEntity, UserVO.class);
                    final String newAccessToken = JwtTokenProvider.createAccessToken(userVO);
                    final String newRefreshToken = JwtTokenProvider.createRefreshToken(userVO, newAccessToken);
                    tableMemberEntity.setToken(newRefreshToken);
                    SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(newAccessToken));
                    AccessBeanUtils.getBean(TableMemberRepository.class).updateMap(TableMemberEntity.class, Map.of("token", newRefreshToken), Map.of("id", tableMemberEntity.getId()));
                    log.debug("refresh token");
                    (response).addHeader("accessToken", newAccessToken);
                    (response).addHeader("refreshToken", newRefreshToken);
                    (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
        stopWatch.stop();
        log.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, requestURI, stopWatch.toString());
    }
}
