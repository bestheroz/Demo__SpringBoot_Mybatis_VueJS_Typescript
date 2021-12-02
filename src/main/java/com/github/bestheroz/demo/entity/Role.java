package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.role.RoleSimpleDTO;
import com.github.bestheroz.demo.repository.RoleRepository;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role implements Serializable {
  @Serial private static final long serialVersionUID = 8475626710152801949L;

  private Long id;
  private Long parentId;
  private String name;
  private Boolean available;
  private Integer displayOrder;

  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;

  public void setParentId(final Long parentId) {
    this.parentId = parentId;
  }

  public void change(RoleRepository roleRepository, final RoleSimpleDTO dto) {
    this.name = dto.getName();
    this.available = dto.getAvailable();
    roleRepository.updateById(this, this.id);
  }
}
