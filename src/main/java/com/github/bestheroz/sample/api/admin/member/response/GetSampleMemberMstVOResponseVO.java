package com.github.bestheroz.sample.api.admin.member.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GetSampleMemberMstVOResponseVO {
    @ApiModelProperty(value = "회원 아이디")
    private String memberId;
    @ApiModelProperty(value = "회원 명")
    private String memberName;
    @ApiModelProperty(value = "회원 타입")
    private String memberType;
    @ApiModelProperty(value = "로그인 실패 건수")
    private Integer loginFailCnt;
    @ApiModelProperty(value = "계정 잠김 여부")
    private Boolean closeTf;
    @ApiModelProperty(value = "계정 만료 일시")
    private LocalDateTime expired;
}
