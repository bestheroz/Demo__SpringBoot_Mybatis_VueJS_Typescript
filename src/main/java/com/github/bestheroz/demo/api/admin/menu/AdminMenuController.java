package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.demo.api.menu.MenuService;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin/menus")
public class AdminMenuController {

  @Resource
  private MenuService menuService;

  @Resource
  private AdminMenuService adminMenuService;

  @Resource
  private TableMenuRepository tableMenuRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.menuService.getMenuList());
  }

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItem(
    @PathVariable(value = "id", required = false) final Integer id
  ) {
    return Result.ok(this.tableMenuRepository.getItemByKey(Map.of("id", id)));
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(
    @RequestBody final TableMenuEntity tableMenuEntity
  ) {
    this.tableMenuRepository.insert(tableMenuEntity);
    return Result.created();
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult> patch(
    @PathVariable(value = "id") final Integer id,
    @RequestBody final TableMenuEntity tableMenuEntity
  ) {
    this.tableMenuRepository.updateByKey(tableMenuEntity, Map.of("id", id));
    return Result.ok();
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(
    @PathVariable(value = "id") final Integer id
  ) {
    this.adminMenuService.delete(id);
    return Result.ok();
  }
}
