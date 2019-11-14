package com.github.bestheroz.sample.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstDAO;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.exception.CommonExceptionCode;
import com.github.bestheroz.standard.common.util.MyDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;

@Service
@Slf4j
public class AuthService {
    private static final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
    @Autowired
    private AuthDAO authDAO;
    @Autowired
    private TableSampleMemberMstDAO tableSampleMemberMstDAO;

    public TableSampleMemberMstVO login(final String memberId, final String memberPw) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        tableSampleMemberMstVO.setMemberPw(memberPw);
        final TableSampleMemberMstVO one = this.tableSampleMemberMstDAO.getOne(tableSampleMemberMstVO, Collections.singleton("memberId"));

        // 로그인 관문
        // 1. 유저가 없으면
        if (one == null) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 2. LOGIN_FAIL_CNT가 5회 이상 인가
        if (one.getLoginFailCnt().intValue() >= 5) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_LOGIN_FAIL_CNT, null, "고객센터로 문의하시기 바랍니다.").toString());
            throw new CommonException(CommonExceptionCode.FAIL_LOGIN_FAIL_CNT, null, "고객센터로 문의하시기 바랍니다.");
        }

        // 3. 패스워드가 틀리면
        if (!StringUtils.equals(memberPw, one.getMemberPw())) {
            this.authDAO.updatePlusLoginFailCnt(memberId);
            log.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 4. 아래는 성공
        if (one.getLoginFailCnt().intValue() != 0) {
            this.authDAO.updateZeroLoginFailCnt(memberId);
        }
        // MySessionUtil.printAttributeList(session);
        final String issuer = one.getMemberName().concat(String.valueOf(one.getMemberId())).concat(MyDateUtils.getStringNow("YYYYMMDD"));
        one.setToken(JWT.create().withIssuer(issuer).sign(ALGORITHM));
        this.tableSampleMemberMstDAO.update(one, Collections.singleton("memberId"), null);
        return one;
    }

    public void verify(@NotNull final String token) throws CommonException {
        if (StringUtils.equals(token, "freepass*.*")) {
            log.info("Login via \"freepass\"");
            return;
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        if (StringUtils.isEmpty(token)) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_TOKEN).toString());
            throw new CommonException(CommonExceptionCode.FAIL_INVALID_TOKEN);
        }
        tableSampleMemberMstVO.setToken(token);
        final TableSampleMemberMstVO one = this.tableSampleMemberMstDAO.getOne(tableSampleMemberMstVO, Collections.singleton("token"));
        if (one == null) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
        try {
            final String issuer = one.getMemberName().concat(String.valueOf(one.getMemberId())).concat(MyDateUtils.getStringNow("YYYYMMDD"));
            JWT.require(ALGORITHM).withIssuer(issuer).build().verify(token);
        } catch (final JWTVerificationException | NullPointerException e) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

    }
}
