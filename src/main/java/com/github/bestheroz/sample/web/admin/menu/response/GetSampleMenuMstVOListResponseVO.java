package com.github.bestheroz.sample.web.admin.menu.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetSampleMenuMstVOListResponseVO implements Serializable {
    @ApiModelProperty(value = "메뉴 아이디")
    private Integer menuId;
    @ApiModelProperty(value = "메뉴 명")
    private String menuNm;
    @ApiModelProperty(value = "부모 메뉴 아이디")
    private Integer parMenuId;
    @ApiModelProperty(value = "메뉴 타입")
    private String menuTyp;
    @ApiModelProperty(value = "사용 여부")
    private String useYn;
    @ApiModelProperty(value = "메뉴 권한")
    private Integer power;
    @ApiModelProperty(value = "출력 순서")
    private Integer dispSeq;
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

    public String getMenuNm() {
        return this.menuNm;
    }

    public void setMenuNm(final String menuNm) {
        this.menuNm = menuNm;
    }

    public Integer getParMenuId() {
        return this.parMenuId;
    }

    public void setParMenuId(final Integer parMenuId) {
        this.parMenuId = parMenuId;
    }

    public String getMenuTyp() {
        return this.menuTyp;
    }

    public void setMenuTyp(final String menuTyp) {
        this.menuTyp = menuTyp;
    }

    public String getUseYn() {
        return this.useYn;
    }

    public void setUseYn(final String useYn) {
        this.useYn = useYn;
    }

    public Integer getPower() {
        return this.power;
    }

    public void setPower(final Integer power) {
        this.power = power;
    }

    public Integer getDispSeq() {
        return this.dispSeq;
    }

    public void setDispSeq(final Integer dispSeq) {
        this.dispSeq = dispSeq;
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
