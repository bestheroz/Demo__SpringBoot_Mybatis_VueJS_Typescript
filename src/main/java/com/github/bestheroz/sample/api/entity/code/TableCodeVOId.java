package com.github.bestheroz.sample.api.entity.code;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableCodeVOId implements Serializable {
    private static final long serialVersionUID = -4897596840013190393L;
    private String codeGroup;
    private String code;

    public TableCodeVOId(final String codeGroup, final String code) {
        this.codeGroup = codeGroup;
        this.code = code;
    }
}
