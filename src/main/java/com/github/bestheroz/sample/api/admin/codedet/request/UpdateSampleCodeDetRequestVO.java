package com.github.bestheroz.sample.api.admin.codedet.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
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
}
