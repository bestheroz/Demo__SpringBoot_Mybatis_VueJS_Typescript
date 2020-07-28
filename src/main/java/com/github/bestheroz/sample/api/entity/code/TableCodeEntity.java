package com.github.bestheroz.sample.api.entity.code;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE")
@IdClass(TableCodeEntityId.class)
public class TableCodeEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = -6076508411557466173L;
    @Id private String codeGroup;
    @Id private String code;
    private String name;
    private boolean available;
    private Integer displayOrder;
    private Integer authority;
}
