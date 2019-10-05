package com.github.bestheroz.sample.api.admin.member.request;

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
    private String memberName;
    @NotEmpty
    @ApiModelProperty(value = "회원 타입", required = true)
    private String memberType;
    @ApiModelProperty(value = "로그인 실패 건수")
    private Integer loginFailCnt;
    @ApiModelProperty(value = "계정 잠김 여부")
    private Boolean closeTf;
    @ApiModelProperty(value = "계정 만료 일시")
    private LocalDateTime expired;

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

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(final String memberName) {
        this.memberName = memberName;
    }

    public String getMemberType() {
        return this.memberType;
    }

    public void setMemberType(final String memberType) {
        this.memberType = memberType;
    }

    public Integer getLoginFailCnt() {
        return this.loginFailCnt;
    }

    public void setLoginFailCnt(final Integer loginFailCnt) {
        this.loginFailCnt = loginFailCnt;
    }

    public Boolean getCloseTf() {
        return this.closeTf;
    }

    public void setCloseTf(final Boolean closeTf) {
        this.closeTf = closeTf;
    }

    public LocalDateTime getExpired() {
        return this.expired;
    }

    public void setExpired(final LocalDateTime expired) {
        this.expired = expired;
    }

}
