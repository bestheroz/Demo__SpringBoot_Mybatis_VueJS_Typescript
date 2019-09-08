package com.github.bestheroz.sample.web.admin.codemst.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class UpdateSampleCodeMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "그룹 코드 명", required = true)
    private String grcodeNm;
    @ApiModelProperty(value = "비고")
    private String remark1;

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
