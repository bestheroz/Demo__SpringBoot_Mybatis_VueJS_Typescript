package com.github.bestheroz.demo.api.role.menu;

import com.github.bestheroz.demo.api.menu.MenuChildrenDTO;
import com.github.bestheroz.demo.api.menu.MenuSimpleDTO;
import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.entity.RoleMenuMap;
import com.github.bestheroz.demo.type.RoleAuthorityType;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleMenuChildrenDTO {
  private Long id;
  @NotNull private MenuSimpleDTO menu;

  @NotNull private List<RoleAuthorityType> authoritiesJson = new ArrayList<>();
  @NotNull private List<RoleMenuChildrenDTO> children = new ArrayList<>();

  public RoleMenuChildrenDTO(final MenuChildrenDTO menuChildrenDTO) {
    this.menu = new MenuSimpleDTO(menuChildrenDTO);
    this.authoritiesJson.addAll(
        Set.of(
            RoleAuthorityType.VIEW,
            RoleAuthorityType.EXCEL,
            RoleAuthorityType.WRITE,
            RoleAuthorityType.DELETE));
    if (NullUtils.isNotEmpty(menuChildrenDTO.getChildren())) {
      this.children.addAll(
          menuChildrenDTO.getChildren().stream()
              .map(RoleMenuChildrenDTO::new)
              .collect(Collectors.toList()));
    }
  }

  public RoleMenuChildrenDTO(
      final RoleMenuMap roleMenuMap, final Menu menu, final List<RoleMenuChildrenDTO> children) {
    this.id = roleMenuMap.getId();
    this.menu = new MenuSimpleDTO(menu);
    this.authoritiesJson = List.copyOf(roleMenuMap.getAuthoritiesJson());
    this.children = children;
  }

  public RoleMenuMap toRoleMenuMap(
      final Long parentId, final Long roleId, final Integer displayOrder) {
    return RoleMenuMap.builder()
        .id(this.id)
        .authoritiesJson(Set.copyOf(this.authoritiesJson))
        .menuId(this.menu.getId())
        .parentId(parentId)
        .roleId(roleId)
        .displayOrder(displayOrder)
        .build();
  }
}
