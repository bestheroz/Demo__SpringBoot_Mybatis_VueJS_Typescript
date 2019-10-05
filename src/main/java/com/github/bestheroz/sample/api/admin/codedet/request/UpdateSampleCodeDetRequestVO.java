package com.github.bestheroz.sample.api.admin.codedet.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateSampleCodeDetRequestVO {
    @NotEmpty
    @ApiModelProperty(value = "코드 명", required = true)
    private String codeName;
    @NotEmpty
    @ApiModelProperty(value = "사용 여부", required = true)
    private Boolean useTf;
    @NotNull
    @ApiModelProperty(value = "출력 순서", required = true)
    private Integer displayOrder;
    @ApiModelProperty(value = "비고")
    private String remark1;

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
