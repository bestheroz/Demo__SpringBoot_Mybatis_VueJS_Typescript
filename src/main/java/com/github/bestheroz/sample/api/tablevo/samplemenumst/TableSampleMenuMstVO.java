package com.github.bestheroz.sample.api.tablevo.samplemenumst;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableSampleMenuMstVO implements Serializable {
    private Integer menuId;
    private String menuNm;
    private String menuTyp;
    private Integer parMenuId;
    private Boolean useTf;
    private Integer power;
    private Integer displayOrder;
    private String url;
    private String remark1;
    private String regMemberId;
    private LocalDateTime regDt;
    private String updMemberId;
    private LocalDateTime updDt;

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

    public String getRegMemberId() {
        return this.regMemberId;
    }

    public void setRegMemberId(final String regMemberId) {
        this.regMemberId = regMemberId;
    }

    public LocalDateTime getRegDt() {
        return this.regDt;
    }

    public void setRegDt(final LocalDateTime regDt) {
        this.regDt = regDt;
    }

    public String getUpdMemberId() {
        return this.updMemberId;
    }

    public void setUpdMemberId(final String updMemberId) {
        this.updMemberId = updMemberId;
    }

    public LocalDateTime getUpdDt() {
        return this.updDt;
    }

    public void setUpdDt(final LocalDateTime updDt) {
        this.updDt = updDt;
    }

}
