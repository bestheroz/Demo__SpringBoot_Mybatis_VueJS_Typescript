package com.github.bestheroz.sample.api.tablevo.samplecodedet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class TableSampleCodeDetVO implements Serializable {
    private static final long serialVersionUID = -6076508411557466173L;
    private String groupCode;
    private String code;
    private String codeName;
    private Boolean useTf;
    private Integer displayOrder;
    private String remark1;
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;
}
