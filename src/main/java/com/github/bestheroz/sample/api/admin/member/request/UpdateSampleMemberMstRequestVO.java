package com.github.bestheroz.sample.api.admin.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateSampleMemberMstRequestVO {
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
}
