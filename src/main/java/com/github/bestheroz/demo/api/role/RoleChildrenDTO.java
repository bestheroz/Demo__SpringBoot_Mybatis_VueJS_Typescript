package com.github.bestheroz.demo.api.role;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.entity.Role.RoleBuilder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleChildrenDTO {
  private Long id;

  @NotEmpty private String name;

  @NotNull private Boolean available;

  @NotNull private List<RoleChildrenDTO> children = new ArrayList<>();

  private Long createdBy;
  private Instant created;

  private Long updatedBy;
  private Instant updated;

  public RoleChildrenDTO(final Role role, final List<RoleChildrenDTO> children) {
    this.id = role.getId();
    this.name = role.getName();
    this.available = role.getAvailable();
    this.children = children;
    this.created = role.getCreated();
    this.createdBy = role.getCreatedBy();
    this.updated = role.getUpdated();
    this.updatedBy = role.getUpdatedBy();
  }

  public Role toRole(final Role parent, final Integer displayOrder) {
    final RoleBuilder roleBuilder =
        Role.builder()
            .id(this.id)
            .name(this.name)
            .available(this.available)
            .displayOrder(displayOrder);
    if (parent != null) {
      roleBuilder.parentId(parent.getId());
    }
    return roleBuilder.build();
  }
}
