package com.github.bestheroz.standard.common.valuelabel.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GetValueLabeVOListResponseVO implements Serializable {
    private static final long serialVersionUID = -3371549792545400090L;
    @ApiModelProperty(value = "코드 명")
    private String label;
    @ApiModelProperty(value = "코드 값")
    private String value;
}
