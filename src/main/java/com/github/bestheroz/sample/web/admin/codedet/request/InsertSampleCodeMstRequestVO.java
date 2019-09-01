package com.github.bestheroz.sample.web.admin.codedet.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class InsertSampleCodeMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "그룹 코드", required = true)
    private String grcode;
    @NotEmpty
    @ApiModelProperty(value = "그룹 코드 명", required = true)
    private String grcodeNm;
    @ApiModelProperty(value = "비고")
    private String remark1;

    public String getGrcode() {
        return this.grcode;
    }

    public void setGrcode(final String grcode) {
        this.grcode = grcode;
    }

    public String getGrcodeNm() {
        return this.grcodeNm;
    }

    public void setGrcodeNm(final String grcodeNm) {
        this.grcodeNm = grcodeNm;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
