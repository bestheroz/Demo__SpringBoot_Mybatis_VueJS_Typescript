package com.github.bestheroz.demo.api.role;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.entity.Role.RoleBuilder;
import java.time.Instant;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleSimpleDTO {
  private Long id;

  @NotEmpty private String name;

  @NotNull private Boolean available;

  private Long createdBy;
  private Instant created;

  private Long updatedBy;
  private Instant updated;

  public RoleSimpleDTO(final Role role) {
    this.id = role.getId();
    this.name = role.getName();
    this.available = role.getAvailable();
    this.created = role.getCreated();
    this.createdBy = role.getCreatedBy();
    this.updated = role.getUpdated();
    this.updatedBy = role.getUpdatedBy();
  }

  public RoleSimpleDTO(final RoleChildrenDTO dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.available = dto.getAvailable();
    this.created = dto.getCreated();
    this.createdBy = dto.getCreatedBy();
    this.updated = dto.getUpdated();
    this.updatedBy = dto.getUpdatedBy();
  }

  public Role toRole(final Integer displayOrder) {
    return Role.builder()
        .id(this.id)
        .name(this.name)
        .available(this.available)
        .displayOrder(displayOrder)
        .build();
  }

  public Role toRole(final Long parentId, final Integer displayOrder) {
    final RoleBuilder roleBuilder =
        Role.builder()
            .id(this.id)
            .name(this.name)
            .available(this.available)
            .parentId(parentId)
            .displayOrder(displayOrder);
    return roleBuilder.build();
  }
}
