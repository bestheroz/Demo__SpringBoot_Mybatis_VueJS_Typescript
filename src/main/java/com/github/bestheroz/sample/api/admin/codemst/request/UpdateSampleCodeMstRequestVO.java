package com.github.bestheroz.sample.api.admin.codemst.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class UpdateSampleCodeMstRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "그룹 코드 명", required = true)
    private String groupCodeNm;
    @ApiModelProperty(value = "비고")
    private String remark1;

    public String getGroupCodeNm() {
        return this.groupCodeNm;
    }

    public void setGroupCodeNm(final String groupCodeNm) {
        this.groupCodeNm = groupCodeNm;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
