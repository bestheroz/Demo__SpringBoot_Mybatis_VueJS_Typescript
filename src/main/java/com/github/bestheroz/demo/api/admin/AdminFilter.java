package com.github.bestheroz.demo.api.admin;

import com.github.bestheroz.standard.common.mybatis.DataTableFilterDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminFilter extends DataTableFilterDTO {
  private List<Boolean> available = new ArrayList<>();
  private List<Long> roleId = new ArrayList<>();

  private final Set<String> searchColumns = Set.of("loginId", "name");

  @Override
  public Map<String, Object> getFilter() {
    if (!this.available.isEmpty()) {
      super.getFilter().put("available:in", Set.copyOf(this.available));
    }
    if (!this.roleId.isEmpty()) {
      super.getFilter().put("roleId:in", Set.copyOf(this.roleId));
    }
    return super.getFilter();
  }
}
