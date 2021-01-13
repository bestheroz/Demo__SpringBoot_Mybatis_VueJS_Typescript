package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin/member/menus/{authority}")
public class AdminMemberMenuController {
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems(@PathVariable(value = "authority") final Integer authority) {
    return Result.ok(
        this.tableMemberMenuRepository.getItemsByKeyWithOrder(
            Map.of("authority", authority), List.of("displayOrder", "name")));
  }

  @PostMapping(value = "save")
  public ResponseEntity<ApiResult> save(
      @PathVariable(value = "authority") final Integer authority,
      @RequestBody final List<TableMemberMenuEntity> menus) {
    this.tableMemberMenuRepository.deleteByKey(
        Map.of(
            "authority",
            authority,
            "id:notIn",
            menus.stream().map(TableMemberMenuEntity::getId).collect(Collectors.toList())));
    menus.forEach(
        menu -> {
          if (this.tableMemberMenuRepository.countByKey(
                  Map.of("authority", authority, "id", menu.getId()))
              == 0) {
            this.tableMemberMenuRepository.insert(menu);
          } else {
            this.tableMemberMenuRepository.updateByKey(
                menu, Map.of("authority", authority, "id", menu.getId()));
          }
        });
    return Result.created();
  }
}
