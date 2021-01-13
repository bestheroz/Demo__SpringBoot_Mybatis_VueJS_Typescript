package com.github.bestheroz.demo.api.admin.member;

import com.github.bestheroz.demo.api.entity.member.TableMemberEntity;
import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.mybatis.DataTableFilterDTO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/members")
public class AdminMemberController {
  @Resource private TableMemberRepository tableMemberRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems(final DataTableFilterDTO dataTableFilterDTO) {
    final int count = this.tableMemberRepository.countForDataTable(dataTableFilterDTO);
    return Result.ok(
        count > 0 ? this.tableMemberRepository.getItemsForDataTable(dataTableFilterDTO) : List.of(),
        count);
  }

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItem(@PathVariable(value = "id") final String id) {
    return Result.ok(
        this.tableMemberRepository
            .getItemByKey(Map.of("id", id))
            .map(
                item -> {
                  item.setPassword(null);
                  return item;
                }));
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(@RequestBody final TableMemberEntity tableMemberEntity) {
    this.tableMemberRepository.insert(tableMemberEntity);
    return Result.created();
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult> patch(
      @PathVariable(value = "id") final String id,
      @RequestBody final TableMemberEntity tableMemberEntity) {
    this.tableMemberRepository.updateMapByKey(
        Map.of(
            "name",
            tableMemberEntity.getName(),
            "authority",
            tableMemberEntity.getAuthority(),
            "expired",
            tableMemberEntity.getExpired(),
            "available",
            tableMemberEntity.isAvailable()),
        Map.of("id", id));
    return Result.ok();
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final String id) {
    this.tableMemberRepository.deleteByKey(Map.of("id", id));
    return Result.ok();
  }

  @PostMapping(value = "{id}/resetPassword")
  public ResponseEntity<ApiResult> resetPassword(@PathVariable(value = "id") final String id) {
    this.tableMemberRepository.resetPassword(id);
    return Result.ok();
  }
}
