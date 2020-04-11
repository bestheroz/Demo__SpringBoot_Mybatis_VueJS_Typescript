package com.github.bestheroz.sample.api.entity.samplecodedet;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableSampleCodeDetId implements Serializable {
    private static final long serialVersionUID = -4897596840013190393L;
    private String groupCode;
    private String code;

    public TableSampleCodeDetId(final String groupCode, final String code) {
        this.groupCode = groupCode;
        this.code = code;
    }
}
