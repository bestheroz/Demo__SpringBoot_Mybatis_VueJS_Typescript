package com.github.bestheroz.sample.api.entity.samplecodedet;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SAMPLE_CODE_DET")
@IdClass(TableSampleCodeDetId.class)
public class TableSampleCodeDetVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = -6076508411557466173L;
    @Id
    private String groupCode;
    @Id
    private String code;
    private String codeName;
    private Boolean useTf;
    private Integer displayOrder;
    private String remark1;
}
