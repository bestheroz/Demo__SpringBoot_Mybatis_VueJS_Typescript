package com.github.bestheroz.sample.api.entity.code;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CODE")
@IdClass(TableCodeVOId.class)
public class TableCodeVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = -6076508411557466173L;
    @Id private String codeGroup;
    @Id private String code;
    private String name;
    private boolean available;
    private Integer displayOrder;
    private Integer authority;
}
