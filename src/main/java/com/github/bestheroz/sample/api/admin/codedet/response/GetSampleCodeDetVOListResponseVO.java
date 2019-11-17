package com.github.bestheroz.sample.api.admin.codedet.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
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
}
