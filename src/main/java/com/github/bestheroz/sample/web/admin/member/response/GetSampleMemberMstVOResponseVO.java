package com.github.bestheroz.sample.web.admin.member.response;

import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDateTime;

public class GetSampleMemberMstVOResponseVO {
    @ApiModelProperty(value = "회원 아이디")
    private String memberId;
    @ApiModelProperty(value = "회원 명")
    private String memberNm;
    @ApiModelProperty(value = "회원 타입")
    private String memberTyp;
    @ApiModelProperty(value = "로그인 실패 건수")
    private Integer loginFailCnt;
    @ApiModelProperty(value = "계정 잠김 여부")
    private String closeYn;
    @ApiModelProperty(value = "계정 만료 일시")
    private LocalDateTime expireDt;

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
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

    public String getCloseYn() {
        return this.closeYn;
    }

    public void setCloseYn(final String closeYn) {
        this.closeYn = closeYn;
    }

    public LocalDateTime getExpireDt() {
        return this.expireDt;
    }

    public void setExpireDt(final LocalDateTime expireDt) {
        this.expireDt = expireDt;
    }
}
