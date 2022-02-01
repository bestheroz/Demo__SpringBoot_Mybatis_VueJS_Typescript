package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.role.menu.RoleMenuService;
import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/menus/")
@RequiredArgsConstructor
public class MenuController {
  private final MenuService menuService;
  private final MenuRepository menuRepository;
  private final RoleMenuService roleMenuService;
  private final RoleRepository roleRepository;

  @GetMapping
  public ResponseEntity<ApiResult<List<MenuChildrenDTO>>> getItems(
      @RequestParam(value = "roleId", required = false) final Long roleId,
      @RequestParam(value = "childRoleId", required = false) final Long childRoleId) {
    if (roleId != null) {
      return Result.ok(
          MenuService.getMenuChildrenDTOWithRecursiveChildren(
              this.roleMenuService.getItems(roleId)));
    } else if (childRoleId != null) {
      return Result.ok(
          this.roleRepository
              .getItemById(childRoleId)
              .map(
                  r -> {
                    if (r.getParentId() == null) {
                      return this.menuService.getItems();
                    } else {
                      return MenuService.getMenuChildrenDTOWithRecursiveChildren(
                          this.roleMenuService.getItems(r.getParentId()));
                    }
                  })
              .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS)));
    }
    return Result.ok(this.menuService.getItems());
  }

  @PostMapping
  public ResponseEntity<ApiResult<MenuChildrenDTO>> post(
      @RequestBody @Valid final MenuSimpleDTO payload) {
    final Menu menu = payload.toMenu(1000);
    this.menuRepository.insert(menu);
    return Result.created(
        new MenuChildrenDTO(
            this.menuRepository
                .getItemById(menu.getId())
                .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS),
            List.of()));
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult<MenuChildrenDTO>> put(
      @PathVariable(value = "id") final Long id, @RequestBody @Valid final MenuSimpleDTO payload) {
    return Result.ok(
        this.menuRepository
            .getItemById(id)
            .map(
                (menu) -> {
                  menu.changeMenu(payload);
                  this.menuRepository.updateById(menu, id);
                  return this.menuRepository
                      .getItemById(menu.getId())
                      .map(r -> new MenuChildrenDTO(r, this.menuService.getChildren(r.getId())))
                      .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
                })
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS)));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult<?>> delete(@PathVariable(value = "id") final Long id) {
    this.menuService.deleteById(id);
    return Result.ok();
  }

  @PostMapping(value = "save-all/")
  public ResponseEntity<ApiResult<List<MenuChildrenDTO>>> saveAll(
      @RequestBody @Valid final List<MenuChildrenDTO> payload) {
    return Result.created(this.menuService.saveAll(payload));
  }
}
