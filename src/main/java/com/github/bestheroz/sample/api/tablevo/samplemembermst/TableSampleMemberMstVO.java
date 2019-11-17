package com.github.bestheroz.sample.api.tablevo.samplemembermst;

import lombok.Data;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Data
public class TableSampleMemberMstVO implements Serializable {
    private static final long serialVersionUID = 7280716056600887400L;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberType;
    private Integer loginFailCnt;
    private LocalDateTime expired;
    private Boolean closeTf;
    private String token;
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;
}
