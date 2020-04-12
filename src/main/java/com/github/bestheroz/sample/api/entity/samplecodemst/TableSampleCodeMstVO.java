package com.github.bestheroz.sample.api.entity.samplecodemst;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SAMPLE_CODE_MST")
public class TableSampleCodeMstVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    @Id
    private String groupCode;
    private String name;
    private String remark1;
}
