package com.github.bestheroz.demo.api.entity.menu;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableMenuEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {
  private static final long serialVersionUID = 2658557582464222508L;
  private Integer id;
  private String name;
  private String type;
  private Integer parentId;
  private Integer displayOrder;
  private String url;
  private String icon;
}
