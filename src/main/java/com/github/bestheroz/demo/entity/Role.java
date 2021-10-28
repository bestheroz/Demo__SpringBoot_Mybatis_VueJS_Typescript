package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.role.RoleSimpleDTO;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends AbstractCreatedUpdate implements Serializable {
  private static final long serialVersionUID = 8475626710152801949L;

  private Long id;
  private Long parentId;
  private String name;
  private Boolean available;
  private Integer displayOrder;

  public void setParentId(final Long parentId) {
    this.parentId = parentId;
  }

  public void change(final RoleSimpleDTO dto) {
    this.name = dto.getName();
    this.available = dto.getAvailable();
  }
}
