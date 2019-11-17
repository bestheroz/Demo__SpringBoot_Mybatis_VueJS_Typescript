package com.github.bestheroz.sample.api.admin.menu.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GetSampleMenuMstVOListResponseVO implements Serializable {
    private static final long serialVersionUID = -3103351411677040021L;
    @ApiModelProperty(value = "메뉴 아이디")
    private Integer menuId;
    @ApiModelProperty(value = "메뉴 명")
    private String menuName;
    @ApiModelProperty(value = "부모 메뉴 아이디")
    private Integer parMenuId;
    @ApiModelProperty(value = "메뉴 타입")
    private String menuType;
    @ApiModelProperty(value = "사용 여부")
    private Boolean useTf;
    @ApiModelProperty(value = "메뉴 권한")
    private Integer power;
    @ApiModelProperty(value = "출력 순서")
    private Integer displayOrder;
    @ApiModelProperty(value = "링크 URL")
    private String url;
    @ApiModelProperty(value = "비고")
    private String remark1;
}
