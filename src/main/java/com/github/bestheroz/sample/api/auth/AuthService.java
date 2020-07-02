package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AuthService implements UserDetailsService {
    @Resource private TableMemberRepository tableMemberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (org.springframework.util.StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("No user found");
        }
        final Optional<TableMemberVO> oTableMemberVO = this.tableMemberRepository.findById(username);
        if (!oTableMemberVO.isPresent()) {
            throw new UsernameNotFoundException("No user found by `" + username + "`");
        }
        return new UserVO(oTableMemberVO.get());
    }

    Map<String, String> login(final String id, final String password) {
        final Optional<TableMemberVO> one = this.tableMemberRepository.findById(id);
        // 로그인 관문
        // 1. 유저가 없으면
        if (!one.isPresent()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final TableMemberVO tableMemberVO = one.get();
        final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        // 2. 패스워드가 틀리면
        if (!pbkdf2PasswordEncoder.matches(tableMemberVO.getPassword(), pbkdf2PasswordEncoder.encode(password))) {
            tableMemberVO.setLoginFailCnt(tableMemberVO.getLoginFailCnt() + 1);
            this.tableMemberRepository.plusLoginFailCnt(tableMemberVO.getId());
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 3. LOGIN_FAIL_CNT가 5회 이상 인가
        if (tableMemberVO.getLoginFailCnt() >= 5) {
            log.warn(ExceptionCode.FAIL_LOGIN_FAIL_CNT.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
        }

        // 4. 계정 차단된 상태인가
        if (!tableMemberVO.isAvailable()) {
            log.warn(ExceptionCode.FAIL_LOGIN_CLOSED.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_CLOSED);
        }

        tableMemberVO.setLoginFailCnt(0);
        final String accessToken = JwtTokenProvider.createAccessToken(tableMemberVO.getId());
        final String refreshToken = JwtTokenProvider.createRefreshToken(tableMemberVO.getId(), accessToken);
        tableMemberVO.setToken(refreshToken);
        this.tableMemberRepository.save(tableMemberVO);
        final Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    void logout() {
        final Optional<TableMemberVO> one = this.tableMemberRepository.findById(AuthenticationUtils.getUserPk());
        if (one.isPresent()) {
            final TableMemberVO tableMemberVO = one.get();
            tableMemberVO.setToken(null);
            this.tableMemberRepository.save(tableMemberVO);
        }
    }
}
