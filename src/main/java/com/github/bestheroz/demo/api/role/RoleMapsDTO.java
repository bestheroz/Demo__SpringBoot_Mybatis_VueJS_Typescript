package com.github.bestheroz.demo.api.role;

import com.github.bestheroz.demo.api.role.menu.RoleMenuChildrenDTO;
import com.github.bestheroz.demo.entity.Role;
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
public class RoleMapsDTO {
  private Long id;

  @NotEmpty private String name;

  @NotNull private Boolean available;

  @NotNull private List<RoleMenuChildrenDTO> maps = new ArrayList<>();

  private Long createdBy;
  private Instant created;

  private Long updatedBy;
  private Instant updated;

  public RoleMapsDTO(final Role role, final List<RoleMenuChildrenDTO> maps) {
    this.id = role.getId();
    this.name = role.getName();
    this.available = role.getAvailable();
    this.maps = maps;
    this.created = role.getCreated();
    this.createdBy = role.getCreatedBy();
    this.updated = role.getUpdated();
    this.updatedBy = role.getUpdatedBy();
  }
}
