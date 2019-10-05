package com.github.bestheroz.sample.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstDAO;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.exception.CommonExceptionCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthDAO authDAO;
    @Autowired
    private TableSampleMemberMstDAO tableSampleMemberMstDAO;

    public void login(final String memberId, final String memberPw) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setMemberId(memberId);
        tableSampleMemberMstVO.setMemberPw(memberPw);
        final TableSampleMemberMstVO sampleMemberMstVO = this.tableSampleMemberMstDAO.getVO(tableSampleMemberMstVO, Collections.singleton("memberId"));

        // 로그인 관문
        // 1. 유저가 없으면
        if (sampleMemberMstVO == null) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).getJsonObject().toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 2. LOGIN_FAIL_CNT가 5회 이상 인가
        if (sampleMemberMstVO.getLoginFailCnt().intValue() >= 5) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_LOGIN_FAIL_CNT, null, "고객센터로 문의하시기 바랍니다.").getJsonObject().toString());
            throw new CommonException(CommonExceptionCode.FAIL_LOGIN_FAIL_CNT, null, "고객센터로 문의하시기 바랍니다.");
        }

        // 3. 패스워드가 틀리면
        if (!StringUtils.equals(memberPw, sampleMemberMstVO.getMemberPw())) {
            this.authDAO.updatePlusLoginFailCnt(memberId);
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).getJsonObject().toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }

        // 4. 아래는 성공
        if (sampleMemberMstVO.getLoginFailCnt().intValue() != 0) {
            this.authDAO.updateZeroLoginFailCnt(memberId);
        }
        // MySessionUtil.printAttributeList(session);
        final Algorithm algorithm = Algorithm.HMAC256("secret");
        final String token = JWT.create().withIssuer("auth0").sign(algorithm);
        sampleMemberMstVO.setToken(token);
        this.tableSampleMemberMstDAO.update(sampleMemberMstVO, Collections.singleton("id"), null);
    }

    void verify(final String token) {
        try {
            final Algorithm algorithm = Algorithm.HMAC256("secret");
            final JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); //Reusable verifier instance
            final DecodedJWT jwt = verifier.verify(token);
        } catch (final JWTVerificationException e) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).getJsonObject().toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = new TableSampleMemberMstVO();
        tableSampleMemberMstVO.setToken(token);
        if (this.tableSampleMemberMstDAO.getVO(tableSampleMemberMstVO, Collections.singleton("token")) == null) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER).getJsonObject().toString());
            throw new CommonException(CommonExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
        }
    }
}
