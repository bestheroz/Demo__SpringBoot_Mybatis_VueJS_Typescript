package com.github.bestheroz.sample.api.auth.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@ToString
@Getter
@Setter
public class LoginRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "회원 아이디", required = true)
    private String memberId;
    @NotEmpty
    @ApiModelProperty(value = "회원 비밀번호(SHA512)", required = true)
    private String memberPw;
}
