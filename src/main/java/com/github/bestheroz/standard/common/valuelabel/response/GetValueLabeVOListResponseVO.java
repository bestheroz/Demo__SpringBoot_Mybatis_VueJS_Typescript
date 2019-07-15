package com.github.bestheroz.standard.common.valuelabel.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetValueLabeVOListResponseVO implements Serializable {
    @ApiModelProperty(value = "코드 명")
    private String label;
    @ApiModelProperty(value = "코드 값")
    private String value;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
