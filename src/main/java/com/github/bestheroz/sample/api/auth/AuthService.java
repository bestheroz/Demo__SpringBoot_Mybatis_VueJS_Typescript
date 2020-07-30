package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.sample.api.entity.member.TableMemberEntity;
import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AuthService implements UserDetailsService {
    @Resource private TableMemberRepository tableMemberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("No user found");
        }
        final Optional<TableMemberEntity> oTableMemberVO = this.tableMemberRepository.getItem(TableMemberEntity.class, Map.of("id", username));
        if (oTableMemberVO.isEmpty()) {
            throw new UsernameNotFoundException("No user found by `" + username + "`");
        }
        final TableMemberEntity tableMemberEntity = oTableMemberVO.get();
        return new UserVO(tableMemberEntity.getId(), tableMemberEntity.getName(), tableMemberEntity.getAuthority(), tableMemberEntity.getTimeout());
    }

    Map<String, String> login(final String id, final String password) {
        final Optional<TableMemberEntity> one = this.tableMemberRepository.getItem(TableMemberEntity.class, Map.of("id", id));
        // 로그인 관문
        // 1. 유저가 없으면
        if (one.isEmpty()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final TableMemberEntity tableMemberEntity = one.get();

        // 2. 패스워드를 생성한 적이 없으면
        if (StringUtils.isEmpty(tableMemberEntity.getPassword())) {
            log.info(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD.toString());
            throw new BusinessException(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD);
        }

        final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        // 3. 패스워드가 틀리면
        if (!pbkdf2PasswordEncoder.matches(tableMemberEntity.getPassword(), pbkdf2PasswordEncoder.encode(password))) {
            tableMemberEntity.setLoginFailCnt(tableMemberEntity.getLoginFailCnt() + 1);
            this.tableMemberRepository.plusLoginFailCnt(tableMemberEntity.getId());
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 4. LOGIN_FAIL_CNT가 5회 이상 인가
        if (tableMemberEntity.getLoginFailCnt() >= 5) {
            log.warn(ExceptionCode.FAIL_LOGIN_FAIL_CNT.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
        }

        // 5. 계정 차단된 상태인가
        if (!tableMemberEntity.isAvailable() || tableMemberEntity.getExpired().toEpochMilli() < Instant.now().toEpochMilli()) {
            log.warn(ExceptionCode.FAIL_LOGIN_CLOSED.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_CLOSED);
        }

        tableMemberEntity.setLoginFailCnt(0);
        final UserVO userVO = MapperUtils.toObject(tableMemberEntity, UserVO.class);
        final String accessToken = JwtTokenProvider.createAccessToken(userVO);
        final String refreshToken = JwtTokenProvider.createRefreshToken(userVO, accessToken);
        this.tableMemberRepository.updateMap(TableMemberEntity.class, Map.of("token", refreshToken), Map.of("id", id));
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    void logout() {
        this.tableMemberRepository.updateMap(TableMemberEntity.class, Map.of("token", ""), Map.of("id", AuthenticationUtils.getUserPk()));
    }

    void initPassword(final String id, final String password) {
        final Optional<TableMemberEntity> one = this.tableMemberRepository.getItem(TableMemberEntity.class, Map.of("id", id));
        // 로그인 관문
        // 1. 유저가 없으면
        if (one.isEmpty()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final TableMemberEntity tableMemberEntity = one.get();
        if (StringUtils.isNotEmpty(tableMemberEntity.getPassword())) {
            log.warn(ExceptionCode.FAIL_INVALID_REQUEST.toString());
            throw new BusinessException(ExceptionCode.FAIL_INVALID_REQUEST);
        }

        this.tableMemberRepository.updateMap(TableMemberEntity.class, Map.of("password", password), Map.of("id", id));
    }
}
