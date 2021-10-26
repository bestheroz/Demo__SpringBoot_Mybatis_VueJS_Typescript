package com.github.bestheroz.demo.api.sign.in;

import com.github.bestheroz.demo.entity.Admin;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SignInService implements UserDetailsService {
  private final AdminRepository adminRepository;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(final String adminId) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(adminId)) {
      throw new UsernameNotFoundException("No user found");
    }
    return this.adminRepository
        .getItemByMap(Map.of("adminId", adminId))
        .map(
            admin -> {
              final Role role =
                  this.roleRepository
                      .getItemById(admin.getRoleId())
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
              return new CustomUserDetails(admin, role);
            })
        .orElseThrow(() -> new UsernameNotFoundException("No user found by `" + adminId + "`"));
  }

  @Transactional(propagation = Propagation.NEVER)
  public Map<String, String> signIn(final String adminId, final String password) {
    return this.adminRepository
        .getItemByMap(Map.of("adminId", adminId))
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
                throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN);
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
              return this.signedSuccess(admin, role);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN));
  }

  private Map<String, String> signedSuccess(final Admin admin, final Role role) {
    admin.resetSignInFailCnt();
    final CustomUserDetails customUserDetails = new CustomUserDetails(admin, role);
    final String refreshToken = JwtTokenProvider.createRefreshToken(customUserDetails);
    admin.signedSuccess(refreshToken);
    this.adminRepository.updateTokenAndSignInFailCnt(admin.getId(), refreshToken);
    final String accessToken = JwtTokenProvider.createAccessToken(customUserDetails);
    return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
  }

  @Transactional(readOnly = true)
  public String getNewAccessToken(final String refreshToken) {
    return this.adminRepository
        .getItemByMap(Map.of("id", JwtTokenProvider.getId(refreshToken), "token", refreshToken))
        .map(
            admin -> {
              final Role role =
                  this.roleRepository
                      .getItemById(admin.getRoleId())
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
              final String newAccessToken =
                  JwtTokenProvider.createAccessToken(new CustomUserDetails(admin, role));
              SecurityContextHolder.getContext()
                  .setAuthentication(JwtTokenProvider.getAuthentication(newAccessToken));
              return newAccessToken;
            })
        .orElseThrow(
            () -> {
              log.info("invalid refresh-token");
              return new BusinessException(ExceptionCode.FAIL_TRY_SIGN_IN_FIRST);
            });
  }
}
