package com.github.bestheroz.sample.api.admin.codemst.response;

import io.swagger.annotations.ApiModelProperty;

public class GetSampleCodeMstVOListResponseVO {
    @ApiModelProperty(value = "그룹 코드")
    private String groupCode;
    @ApiModelProperty(value = "그룹 코드 명")
    private String groupCodeName;
    @ApiModelProperty(value = "비고")
    private String remark1;

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupCode(final String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCodeName() {
        return this.groupCodeName;
    }

    public void setGroupCodeName(final String groupCodeName) {
        this.groupCodeName = groupCodeName;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
