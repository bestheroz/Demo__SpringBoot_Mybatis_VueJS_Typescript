package com.github.bestheroz.demo.api.entity.code;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableCodeEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {

  private static final long serialVersionUID = -6076508411557466173L;
  private String codeGroup;
  private String code;
  private String name;
  private boolean available;
  private Integer displayOrder;
  private Integer authority;
}
