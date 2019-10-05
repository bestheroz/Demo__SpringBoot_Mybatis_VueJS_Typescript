package com.github.bestheroz.sample.api.admin.codedet.response;

import io.swagger.annotations.ApiModelProperty;

public class GetSampleCodeDetVOListResponseVO {
    @ApiModelProperty(value = "그룹 코드")
    private String groupCode;
    @ApiModelProperty(value = "코드")
    private String code;
    @ApiModelProperty(value = "코드 명")
    private String codeName;
    @ApiModelProperty(value = "사용 여부")
    private Boolean useTf;
    @ApiModelProperty(value = "출력 순서")
    private Integer displayOrder;
    @ApiModelProperty(value = "비고")
    private String remark1;

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupCode(final String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(final String codeName) {
        this.codeName = codeName;
    }

    public Boolean getUseTf() {
        return this.useTf;
    }

    public void setUseTf(final Boolean useTf) {
        this.useTf = useTf;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
