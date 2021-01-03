package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin/menus")
public class AdminMenuController {
  @Resource
  private AdminMenuService adminMenuService;
  @Resource
  private TableMenuRepository tableMenuRepository;
  @Resource
  private TableMemberMenuRepository tableMemberMenuRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    if (
      List
        .of(900, 999)
        .contains(AuthenticationUtils.getLoginVO().getAuthority())
    ) {
      return Result.ok(
        this.tableMenuRepository.getItemsWithOrder(
          List.of("displayOrder", "name")
        )
      );
    } else {
      return Result.ok(
        this.tableMemberMenuRepository.getItemsByKeyWithOrder(
          Map.of(
            "authority",
            AuthenticationUtils.getLoginVO().getAuthority()
          ),
          List.of("displayOrder", "name")
        )
      );
    }
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(
    @RequestBody final TableMenuEntity tableMenuEntity
  ) {
    this.tableMenuRepository.insert(tableMenuEntity);
    return Result.created();
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult> put(
    @PathVariable(value = "id") final Integer id,
    @RequestBody final TableMenuEntity tableMenuEntity
  ) {
    this.adminMenuService.put(tableMenuEntity, id);
    return Result.ok();
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(
    @PathVariable(value = "id") final Integer id
  ) {
    this.adminMenuService.delete(id);
    return Result.ok();
  }

  @PostMapping(value = "save")
  public ResponseEntity<ApiResult> save(
    @RequestBody final List<TableMenuEntity> menus
  ) {
    menus.forEach(
      menu ->
        this.tableMenuRepository.updateByKey(menu, Map.of("id", menu.getId()))
    );
    return Result.created();
  }
}
