package com.github.bestheroz.demo.api.mine;

import com.github.bestheroz.demo.api.role.RoleSimpleDTO;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.Role;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MineDTO {
  private Long id;
  private String adminId;
  private String name;
  private RoleSimpleDTO role;
  private Instant created;
  private Instant updated;

  public MineDTO(final Admin admin, final Role role) {
    this.id = admin.getId();
    this.name = admin.getName();
    this.adminId = admin.getAdminId();
    this.role = new RoleSimpleDTO(role);
    this.created = admin.getCreated();
    this.updated = admin.getUpdated();
  }
}
