package com.github.bestheroz.sample.web.admin.valuelabel.response;

import io.swagger.annotations.ApiModelProperty;

public class GetSampleCodeDetVOListResponseVO {
    @ApiModelProperty(value = "그룹 코드")
    private String grcode;
    @ApiModelProperty(value = "코드")
    private String code;
    @ApiModelProperty(value = "코드 명")
    private String codeNm;
    @ApiModelProperty(value = "사용 여부")
    private String useYn;
    @ApiModelProperty(value = "출력 순서")
    private Integer dispSeq;
    @ApiModelProperty(value = "비고")
    private String remark1;

    public String getGrcode() {
        return this.grcode;
    }

    public void setGrcode(final String grcode) {
        this.grcode = grcode;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getCodeNm() {
        return this.codeNm;
    }

    public void setCodeNm(final String codeNm) {
        this.codeNm = codeNm;
    }

    public String getUseYn() {
        return this.useYn;
    }

    public void setUseYn(final String useYn) {
        this.useYn = useYn;
    }

    public Integer getDispSeq() {
        return this.dispSeq;
    }

    public void setDispSeq(final Integer dispSeq) {
        this.dispSeq = dispSeq;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
    }

}
