package com.github.bestheroz.sample.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.entity.samplemembermst.TableSampleMemberMstRepository;
import com.github.bestheroz.sample.api.entity.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private static final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
    @Resource private TableSampleMemberMstRepository tableSampleMemberMstRepository;

    TableSampleMemberMstVO login(final String memberId, final String password) {
        System.out.println(this.tableSampleMemberMstRepository.findAll());
        final Optional<TableSampleMemberMstVO> one = this.tableSampleMemberMstRepository.findById(memberId);
        // 로그인 관문
        // 1. 유저가 없으면
        if (!one.isPresent()) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
        // 2. LOGIN_FAIL_CNT가 5회 이상 인가
        final TableSampleMemberMstVO tableSampleMemberMstVO = one.get();
        if (tableSampleMemberMstVO.getLoginFailCnt() >= 5) {
            log.warn(ExceptionCode.FAIL_LOGIN_FAIL_CNT.toString());
            throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
        }
        // 3. 패스워드가 틀리면
        if (!StringUtils.equals(tableSampleMemberMstVO.getMemberPw(), password)) {
            log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 4. 아래는 성공
        if (tableSampleMemberMstVO.getLoginFailCnt() != 0) {
            this.tableSampleMemberMstRepository.updateLoginFailCntToZero(memberId);
        }

        this.tableSampleMemberMstRepository.updateToken(memberId, JWT.create().withIssuer(memberId).withExpiresAt(LocalDateTime.now().plusDays(1).toDate()).sign(ALGORITHM));
        SessionUtils.setLoginVO(tableSampleMemberMstVO);
        return tableSampleMemberMstVO;
    }

    public void verify(@NotNull final String token, @NotNull final String memberId) {
        try {
            JWT.require(ALGORITHM).withIssuer(memberId).acceptExpiresAt(86400).build().verify(token);
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
    }
}
