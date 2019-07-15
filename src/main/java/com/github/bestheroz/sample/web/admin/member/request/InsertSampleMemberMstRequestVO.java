package com.github.bestheroz.sample.web.admin.member.request;

import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

public class InsertSampleMemberMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "회원 아이디", required = true)
    private String memberId;
    @NotEmpty
    @ApiModelProperty(value = "회원 비밀번호", required = true)
    private String memberPw;
    @NotEmpty
    @ApiModelProperty(value = "회원 명", required = true)
    private String memberNm;
    @NotEmpty
    @ApiModelProperty(value = "회원 타입", required = true)
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
