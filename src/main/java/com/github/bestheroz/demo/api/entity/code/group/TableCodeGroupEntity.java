package com.github.bestheroz.demo.api.entity.code.group;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableCodeGroupEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {

  private static final long serialVersionUID = -9216318893256632523L;
  private String codeGroup;
  private String name;
}
