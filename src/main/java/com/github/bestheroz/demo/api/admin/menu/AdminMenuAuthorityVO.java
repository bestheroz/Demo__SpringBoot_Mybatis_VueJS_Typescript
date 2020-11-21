package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminMenuAuthorityVO extends TableMenuEntity {

  private static final long serialVersionUID = -76896741552627585L;
  private Integer level;
  private boolean checked;
}
