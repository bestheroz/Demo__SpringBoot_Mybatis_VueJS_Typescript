package com.github.bestheroz.standard.common.authenticate;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.util.AccessBeanUtils;
import com.github.bestheroz.standard.context.security.SecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final String token = JwtTokenProvider.resolveAccessToken(request);
        final String refreshToken = JwtTokenProvider.resolveRefreshToken(request);
        final String requestURI = (request).getRequestURI();
        final Optional<String> first = Arrays.stream(SecurityConfiguration.PUBLIC).map(item -> item.replace("*", "")).filter(requestURI::startsWith).findFirst();
        if (StringUtils.isAnyEmpty(token, refreshToken) && first.isEmpty()) {
            (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (token != null) {
            if (JwtTokenProvider.validateAccessToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(token));
            } else if (JwtTokenProvider.validateRefreshToken(token, refreshToken)) {
                final Optional<TableMemberVO> one = AccessBeanUtils.getBean(TableMemberRepository.class).findByToken(refreshToken);
                if (one.isPresent()) {
                    final TableMemberVO tableMemberVO = one.get();
                    final String newAccessToken = JwtTokenProvider.createAccessToken(tableMemberVO.getId());
                    final String newRefreshToken = JwtTokenProvider.createRefreshToken(tableMemberVO.getId(), newAccessToken);
                    tableMemberVO.setToken(newRefreshToken);
                    SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(newAccessToken));
                    AccessBeanUtils.getBean(TableMemberRepository.class).save(tableMemberVO);
                    (response).addHeader("accessToken", newAccessToken);
                    (response).addHeader("refreshToken", newRefreshToken);
                    (response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
