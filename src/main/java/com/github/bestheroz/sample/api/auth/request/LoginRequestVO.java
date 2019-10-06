package com.github.bestheroz.sample.api.auth.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class LoginRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "회원 아이디", required = true)
    private String memberId;
    @NotEmpty
    @ApiModelProperty(value = "회원 비밀번호(SHA512)", required = true)
    private String memberPw;

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
}
