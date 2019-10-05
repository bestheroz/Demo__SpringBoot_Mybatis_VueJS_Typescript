package com.github.bestheroz.sample.api.tablevo.samplemembermst;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableSampleMemberMstVO implements Serializable {
    private String memberId;
    private String memberPw;
    private String memberNm;
    private String memberTyp;
    private Integer loginFailCnt;
    private LocalDateTime expireDt;
    private Boolean isClosed;
    private String token;
    private String regMemberId;
    private LocalDateTime regDt;
    private String updMemberId;
    private LocalDateTime updDt;

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPw() {
        return this.memberPw;
    }

    public void setMemberPw(final String memberPw) {
        this.memberPw = memberPw;
    }

    public String getMemberNm() {
        return this.memberNm;
    }

    public void setMemberNm(final String memberNm) {
        this.memberNm = memberNm;
    }

    public String getMemberTyp() {
        return this.memberTyp;
    }

    public void setMemberTyp(final String memberTyp) {
        this.memberTyp = memberTyp;
    }

    public Integer getLoginFailCnt() {
        return this.loginFailCnt;
    }

    public void setLoginFailCnt(final Integer loginFailCnt) {
        this.loginFailCnt = loginFailCnt;
    }

    public LocalDateTime getExpireDt() {
        return this.expireDt;
    }

    public void setExpireDt(final LocalDateTime expireDt) {
        this.expireDt = expireDt;
    }

    public Boolean getClosed() {
        return this.isClosed;
    }

    public void setClosed(final Boolean closed) {
        this.isClosed = closed;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getRegMemberId() {
        return this.regMemberId;
    }

    public void setRegMemberId(final String regMemberId) {
        this.regMemberId = regMemberId;
    }

    public LocalDateTime getRegDt() {
        return this.regDt;
    }

    public void setRegDt(final LocalDateTime regDt) {
        this.regDt = regDt;
    }

    public String getUpdMemberId() {
        return this.updMemberId;
    }

    public void setUpdMemberId(final String updMemberId) {
        this.updMemberId = updMemberId;
    }

    public LocalDateTime getUpdDt() {
        return this.updDt;
    }

    public void setUpdDt(final LocalDateTime updDt) {
        this.updDt = updDt;
    }
}
