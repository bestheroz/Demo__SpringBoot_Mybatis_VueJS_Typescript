package com.github.bestheroz.demo.api.entity.menuauthority;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMenuAuthorityEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {
  private static final long serialVersionUID = 7975686196090342524L;
  private Integer authority;
  private String menuIdList;
}
