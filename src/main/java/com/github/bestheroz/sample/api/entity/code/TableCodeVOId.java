package com.github.bestheroz.sample.api.entity.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableCodeVOId implements Serializable {
    private static final long serialVersionUID = -4897596840013190393L;
    @Id private String codeGroup;
    @Id private String code;
}
