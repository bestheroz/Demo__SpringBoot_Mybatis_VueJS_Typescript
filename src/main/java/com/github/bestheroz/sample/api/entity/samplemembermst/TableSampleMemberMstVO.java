package com.github.bestheroz.sample.api.entity.samplemembermst;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SAMPLE_MEMBER_MST")
public class TableSampleMemberMstVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 7280716056600887400L;
    @Id
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberType;
    private Integer loginFailCnt;
    private LocalDateTime expired;
    private Boolean closeTf;
    private String token;
}
