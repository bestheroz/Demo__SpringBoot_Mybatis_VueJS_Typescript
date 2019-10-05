package com.github.bestheroz.sample.api.admin.menu.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InsertSampleMenuMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "메뉴 명", required = true)
    private String menuNm;
    @NotEmpty
    @ApiModelProperty(value = "메뉴 타입", required = true)
    private String menuTyp;
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

    public String getMenuNm() {
        return this.menuNm;
    }

    public void setMenuNm(final String menuNm) {
        this.menuNm = menuNm;
    }

    public String getMenuTyp() {
        return this.menuTyp;
    }

    public void setMenuTyp(final String menuTyp) {
        this.menuTyp = menuTyp;
    }

    public Integer getParMenuId() {
        return this.parMenuId;
    }

    public void setParMenuId(final Integer parMenuId) {
        this.parMenuId = parMenuId;
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

    public void setDispSeq(final Integer displayOrder) {
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
