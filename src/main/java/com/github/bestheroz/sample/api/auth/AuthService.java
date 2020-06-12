package com.github.bestheroz.sample.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private static final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
    @Resource private TableMemberRepository tableMemberRepository;


    TableMemberVO login(final String id, final String password) {
        final Optional<TableMemberVO> one = this.tableMemberRepository.findById(id);
        // 로그인 관문
        // 1. 유저가 없으면
        if (!one.isPresent()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final TableMemberVO tableMemberVO = one.get();

        // 2. 패스워드가 틀리면
        if (!StringUtils.equals(tableMemberVO.getPassword(), password)) {
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
//        tableMemberVO.setToken(JWT.create().withIssuer(id).withExpiresAt(LocalDateTime.now().plusDays(1).toDate()).sign(ALGORITHM));
        this.tableMemberRepository.save(tableMemberVO);
        SessionUtils.setLoginVO(tableMemberVO);
        return tableMemberVO;
    }

    void verify(@NotNull final String token, @NotNull final String id) {
        try {
            JWT.require(ALGORITHM).withIssuer(id).acceptExpiresAt(86400).build().verify(token);
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
    }

    Map<String, Object> getMyData(final String id, final String password) {
        final Optional<TableMemberVO> one = this.tableMemberRepository.findById(id);
        // 로그인 관문
        // 1. 유저가 없으면
        if (!one.isPresent()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final TableMemberVO tableMemberVO = one.get();

        // 2. 패스워드가 틀리면
        if (!StringUtils.equals(tableMemberVO.getPassword(), password)) {
            tableMemberVO.setLoginFailCnt(tableMemberVO.getLoginFailCnt() + 1);
            this.tableMemberRepository.plusLoginFailCnt(tableMemberVO.getId());
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        final Map<String, Object> result = new HashMap<>();
        result.put("userVO", tableMemberVO);
        result.put("token", JWT.create().withIssuer(tableMemberVO.getId()).withExpiresAt(LocalDateTime.now().plusDays(1).toDate()).sign(ALGORITHM));
        return result;
    }
}
