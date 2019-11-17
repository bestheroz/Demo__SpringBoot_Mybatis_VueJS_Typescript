package com.github.bestheroz.sample.api.admin.menu.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateSampleMenuMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "메뉴 명", required = true)
    private String menuName;
    @NotEmpty
    @ApiModelProperty(value = "메뉴 타입", required = true)
    private String menuType;
    @NotNull
    @ApiModelProperty(value = "부모 메뉴 아이디", required = true)
    private Integer parMenuId;
    @NotEmpty
    @ApiModelProperty(value = "사용 여부", required = true)
    private Boolean useTf;
    @NotNull
    @ApiModelProperty(value = "메뉴 권한", required = true)
    private Integer power;
    @NotNull
    @ApiModelProperty(value = "출력 순서", required = true)
    private Integer displayOrder;
    @ApiModelProperty(value = "링크 URL")
    private String url;
    @ApiModelProperty(value = "비고")
    private String remark1;
}
