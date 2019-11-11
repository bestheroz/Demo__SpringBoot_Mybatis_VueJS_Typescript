package com.github.bestheroz.sample.api.tablevo.samplecodemst;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class TableSampleCodeMstVO implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    private String groupCode;
    private String groupCodeName;
    private String remark1;
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;
}
