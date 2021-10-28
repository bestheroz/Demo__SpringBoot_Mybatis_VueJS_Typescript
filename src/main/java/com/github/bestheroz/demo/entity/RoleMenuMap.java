package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.type.RoleAuthorityType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleMenuMap implements Serializable {
  private static final long serialVersionUID = -4753709861734048435L;

  private Long id;

  private Long parentId;

  private Long roleId;

  private Long menuId;

  private Integer displayOrder;

  @Builder.Default private Set<RoleAuthorityType> authoritiesJson = new HashSet<>();
}
