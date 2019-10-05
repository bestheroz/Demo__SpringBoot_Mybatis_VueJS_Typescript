package com.github.bestheroz.sample.api.admin.menu.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetSampleMenuMstVOListResponseVO implements Serializable {
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

    public Integer getMenuId() {
        return this.menuId;
    }

    public void setMenuId(final Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(final String menuName) {
        this.menuName = menuName;
    }

    public Integer getParMenuId() {
        return this.parMenuId;
    }

    public void setParMenuId(final Integer parMenuId) {
        this.parMenuId = parMenuId;
    }

    public String getMenuType() {
        return this.menuType;
    }

    public void setMenuType(final String menuType) {
        this.menuType = menuType;
    }

    public Boolean getUseTf() {
        return this.useTf;
    }

    public void setUseTf(final Boolean useTf) {
        this.useTf = useTf;
    }

    public Integer getPower() {
        return this.power;
    }

    public void setPower(final Integer power) {
        this.power = power;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
