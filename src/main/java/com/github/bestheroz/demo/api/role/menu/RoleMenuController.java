package com.github.bestheroz.demo.api.role.menu;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/roles/{roleId}/maps/")
@Slf4j
@RequiredArgsConstructor
public class RoleMenuController {
  private final RoleMenuService roleMenuService;

  @GetMapping
  public ResponseEntity<ApiResult<List<RoleMenuChildrenDTO>>> getItems(
      @PathVariable("roleId") final Long roleId) {
    return Result.ok(this.roleMenuService.getItems(roleId));
  }

  @PostMapping(value = "save-all/")
  public ResponseEntity<ApiResult<List<RoleMenuChildrenDTO>>> saveAll(
      @PathVariable("roleId") final Long roleId,
      @RequestBody @Valid final List<RoleMenuChildrenDTO> payload) {
    return Result.created(this.roleMenuService.saveAll(roleId, payload));
  }
}
