package com.github.bestheroz.demo.api.auth;

import com.github.bestheroz.demo.api.entity.member.TableMemberEntity;
import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.time.Instant;
import java.util.Map;
import javax.annotation.Resource;
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
public class AuthService implements UserDetailsService {
  @Resource
  private TableMemberRepository tableMemberRepository;

  @Override
  public UserDetails loadUserByUsername(final String username)
    throws UsernameNotFoundException {
    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("No user found");
    }
    return this.tableMemberRepository.getItem(
        TableMemberEntity.class,
        Map.of("id", username)
      )
      .map(
        tableMemberEntity ->
          new UserVO(
            tableMemberEntity.getId(),
            tableMemberEntity.getName(),
            tableMemberEntity.getAuthority(),
            tableMemberEntity.getTimeout(),
            tableMemberEntity.getTheme()
          )
      )
      .orElseThrow(
        () ->
          new UsernameNotFoundException("No user found by `" + username + "`")
      );
  }

  Map<String, String> login(final String id, final String password) {
    return this.tableMemberRepository.getItem(
        TableMemberEntity.class,
        Map.of("id", id)
      )
      .map(
        tableMemberEntity -> {
          // 2. 패스워드를 생성한 적이 없으면
          if (StringUtils.isEmpty(tableMemberEntity.getPassword())) {
            log.info(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD.toString());
            throw new BusinessException(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD);
          }

          final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
          // 3. 패스워드가 틀리면
          if (
            !pbkdf2PasswordEncoder.matches(
              tableMemberEntity.getPassword(),
              pbkdf2PasswordEncoder.encode(password)
            )
          ) {
            this.tableMemberRepository.plusLoginFailCnt(
                tableMemberEntity.getId()
              );
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
          }

          // 4. LOGIN_FAIL_CNT가 5회 이상 인가
          if (tableMemberEntity.getLoginFailCnt() >= 5) {
            log.warn(ExceptionCode.FAIL_LOGIN_FAIL_CNT.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
          }

          // 5. 계정 차단된 상태인가
          if (
            !tableMemberEntity.isAvailable() ||
            tableMemberEntity.getExpired().toEpochMilli() <
            Instant.now().toEpochMilli()
          ) {
            log.warn(ExceptionCode.FAIL_LOGIN_CLOSED.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_CLOSED);
          }

          tableMemberEntity.setLoginFailCnt(0);
          final UserVO userVO = MapperUtils.toObject(
            tableMemberEntity,
            UserVO.class
          );
          final String accessToken = JwtTokenProvider.createAccessToken(userVO);
          final String refreshToken = JwtTokenProvider.createRefreshToken(
            userVO
          );
          SecurityContextHolder
            .getContext()
            .setAuthentication(JwtTokenProvider.getAuthentication(accessToken));
          this.tableMemberRepository.updateMap(
              TableMemberEntity.class,
              Map.of("token", refreshToken),
              Map.of("id", id)
            );
          return Map.of(
            "accessToken",
            accessToken,
            "refreshToken",
            refreshToken
          );
        }
      )
      .orElseThrow(
        () -> {
          // 1. 유저가 없으면
          log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
          return new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
      );
  }

  void logout() {
    this.tableMemberRepository.updateMap(
        TableMemberEntity.class,
        Map.of("token", ""),
        Map.of("id", AuthenticationUtils.getUserPk())
      );
  }

  String getNewAccessToken(final String refreshToken) {
    return this.tableMemberRepository.getItem(
        TableMemberEntity.class,
        Map.of(
          "token",
          refreshToken,
          "id",
          JwtTokenProvider.getUserPk(refreshToken)
        )
      )
      .map(
        tableMemberEntity -> {
          final UserVO userVO = MapperUtils.toObject(
            tableMemberEntity,
            UserVO.class
          );
          final String newAccessToken = JwtTokenProvider.createAccessToken(
            userVO
          );
          SecurityContextHolder
            .getContext()
            .setAuthentication(
              JwtTokenProvider.getAuthentication(newAccessToken)
            );
          return newAccessToken;
        }
      )
      .orElseThrow(
        () -> {
          log.info("invalid refresh-token");
          return BusinessException.FAIL_TRY_LOGIN_FIRST;
        }
      );
  }

  void initPassword(final String id, final String password) {
    this.tableMemberRepository.getItem(
        TableMemberEntity.class,
        Map.of("id", id)
      )
      .ifPresentOrElse(
        tableMemberEntity -> {
          if (StringUtils.isNotEmpty(tableMemberEntity.getPassword())) {
            log.warn(ExceptionCode.FAIL_INVALID_REQUEST.toString());
            throw new BusinessException(ExceptionCode.FAIL_INVALID_REQUEST);
          }

          this.tableMemberRepository.updateMap(
              TableMemberEntity.class,
              Map.of("password", password),
              Map.of("id", id)
            );
        },
        () -> {
          // 1. 유저가 없으면
          log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
          throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
      );
  }
}
