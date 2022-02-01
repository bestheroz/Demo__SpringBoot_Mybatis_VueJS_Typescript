package com.github.bestheroz.demo.api.sign.in;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.standard.common.authenticate.CustomUserDetails;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SignInService implements UserDetailsService {
  private final AdminRepository adminRepository;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(loginId)) {
      throw new UsernameNotFoundException("No user found");
    }
    return this.adminRepository
        .getItemByMap(Map.of("loginId", loginId))
        .map(
            admin -> {
              final Role role =
                  this.roleRepository
                      .getItemById(admin.getRoleId())
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
              return new CustomUserDetails(admin, role);
            })
        .orElseThrow(() -> new UsernameNotFoundException("No user found by `" + loginId + "`"));
  }

  public TokenDTO signIn(final String loginId, final String password) {
    return this.adminRepository
        .getItemByMap(Map.of("loginId", loginId))
        .map(
            admin -> {
              final Role role =
                  this.roleRepository
                      .getItemById(admin.getRoleId())
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
              // 1. 역할 체크
              if (!role.getAvailable()) {
                throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN);
              }

              final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
              // 2. 패스워드가 틀리면
              if (!pbkdf2PasswordEncoder.matches(
                  admin.getPassword(), pbkdf2PasswordEncoder.encode(password))) {
                admin.plusSignInFailCnt();
                this.adminRepository.plusSignInFailCnt(admin.getId());
                return new TokenDTO(null, null);
              }

              // 3. SIGN_IN_FAIL_CNT가 5회 이상 인가
              if (admin.getSignInFailCnt() >= 5) {
                throw new BusinessException(ExceptionCode.FAIL_SIGN_IN_FAIL_CNT);
              }

              // 4. 계정 차단된 상태인가
              if (!admin.getAvailable()
                  || admin.getExpired().toEpochMilli() < Instant.now().toEpochMilli()) {
                throw new BusinessException(ExceptionCode.FAIL_SIGN_IN_CLOSED);
              }
              final CustomUserDetails customUserDetails = new CustomUserDetails(admin, role);
              final String accessToken = JwtTokenProvider.createAccessToken(customUserDetails);
              final String refreshToken = JwtTokenProvider.createRefreshToken(customUserDetails);
              admin.signedSuccess(refreshToken);
              this.adminRepository.updateTokenAndSignInFailCnt(admin.getId(), refreshToken);
              return new TokenDTO(accessToken, refreshToken);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN));
  }

  @Transactional
  public TokenDTO getNewToken(final String refreshToken) {
    return this.adminRepository
        .getItemByMap(Map.of("id", JwtTokenProvider.getId(refreshToken), "token", refreshToken))
        .map(
            admin -> {
              final Role role =
                  this.roleRepository
                      .getItemById(admin.getRoleId())
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
              final CustomUserDetails customUserDetails = new CustomUserDetails(admin, role);
              final String newAccessToken = JwtTokenProvider.createAccessToken(customUserDetails);
              SecurityContextHolder.getContext()
                  .setAuthentication(JwtTokenProvider.getAuthentication(newAccessToken));
              // refreshToken 이 생성된지 5초 이내에 요청이 들어오면 존재하는 refreshToken 을 반환하자.
              if (JwtTokenProvider.issuedRefreshTokenIn3Seconds(refreshToken)) {
                return new TokenDTO(newAccessToken, admin.getToken());
              }
              // 동시 로그인을 허용하려면 refreshToken 은 새로 업데이트 하면 안된다.
              final String newRefreshToken = JwtTokenProvider.createRefreshToken(customUserDetails);
              admin.signedSuccess(newRefreshToken);
              this.adminRepository.updateTokenAndSignInFailCnt(admin.getId(), newRefreshToken);
              return new TokenDTO(newAccessToken, newRefreshToken);
            })
        .orElseThrow(
            () -> {
              log.info("invalid refresh-token");
              return new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
            });
  }
}
