package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.entity.member.TableMemberEntity;
import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/members")
public class AdminMemberController {
    @Resource private TableMemberRepository tableMemberRepository;

    @GetMapping
    ResponseEntity<ApiResult> getItems() {
        final List<TableMemberEntity> items = this.tableMemberRepository.getItems(TableMemberEntity.class);
        items.forEach(item -> item.setPassword(null));
        return Result.ok(items);
    }

    @GetMapping(value = "{id}")
    ResponseEntity<ApiResult> getItem(@PathVariable(value = "id") final String id) {
        final Optional<TableMemberEntity> item = this.tableMemberRepository.getItem(TableMemberEntity.class, Map.of("id", id));
        item.ifPresent(item1 -> item1.setPassword(null));
        return Result.ok(item);
    }

    @PostMapping
    @CacheEvict(value = "memberCache")
    public ResponseEntity<ApiResult> insert(@RequestBody final TableMemberEntity tableMemberEntity) {
        this.tableMemberRepository.insert(tableMemberEntity);
        return Result.ok();
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "memberCache")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "id") final String id, @RequestBody final TableMemberEntity tableMemberEntity) {
        this.tableMemberRepository.updateMap(TableMemberEntity.class,
                Map.of("name", tableMemberEntity.getName(), "authority", tableMemberEntity.getAuthority(), "expired", tableMemberEntity.getExpired(), "available", tableMemberEntity.isAvailable(),
                        "timeout", tableMemberEntity.getTimeout()), Map.of("id", id));
        return Result.ok();
    }

    @DeleteMapping(value = "{id}")
    @CacheEvict(value = "memberCache")
    public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final String id) {
        this.tableMemberRepository.delete(TableMemberEntity.class, Map.of("id", id));
        return Result.ok();
    }

    @PostMapping(value = "{id}/resetPassword")
    public ResponseEntity<ApiResult> resetPassword(@PathVariable(value = "id") final String id) {
        this.tableMemberRepository.resetPassword(id);
        return Result.ok();
    }

    @GetMapping("memberList")
    public ResponseEntity<ApiResult> getMemberList() {
        return Result.ok(this.tableMemberRepository.getItems(TableMemberEntity.class).stream().map(item -> new CodeVO(item.getId(), item.getName())).collect(Collectors.toList()));
    }
}
