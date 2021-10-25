package com.github.bestheroz.demo.api.admin;

import com.github.bestheroz.demo.api.role.RoleSimpleDTO;
import com.github.bestheroz.demo.entity.Admin;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminPasswordDTO {
  private Long id;
  private String adminId;
  private String name;
  private String password;
  @NotNull private RoleSimpleDTO role;
  private Boolean available;
  private Instant expired;

  private Long createdBy;
  private Instant created;

  private Long updatedBy;
  private Instant updated;

  public Admin toAdmin() {
    return Admin.builder()
        .adminId(this.adminId)
        .name(this.name)
        .password(this.password)
        .roleId(this.role.getId())
        .available(this.available)
        .expired(this.expired)
        .signInFailCnt(0)
        .build();
  }
}
