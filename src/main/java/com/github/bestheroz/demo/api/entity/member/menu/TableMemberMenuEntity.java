package com.github.bestheroz.demo.api.entity.member.menu;

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
public class TableMemberMenuEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {
  private static final long serialVersionUID = 6518292219807880047L;
  private Integer authority;
  private Integer id;
  private String name;
  private String type;
  private Integer parentId;
  private Integer displayOrder;
  private String icon;
  private String url;
}
