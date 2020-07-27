package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.entity.member.TableMemberEntity;
import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/members")
public class AdminMemberController {
    @Resource private TableMemberRepository tableMemberRepository;

    @GetMapping
    ResponseEntity<ApiResult> getList() {
        return Result.ok(this.tableMemberRepository.findAll());
    }

    @GetMapping(value = "{id}")
    ResponseEntity<ApiResult> getOne(@PathVariable(value = "id") final String id) {
        return Result.ok(this.tableMemberRepository.findById(id));
    }

    @PostMapping
    @CacheEvict(value = "memberCache")
    public ResponseEntity<ApiResult> insert(@RequestBody final TableMemberEntity tableMemberEntity) {
        this.tableMemberRepository.save(tableMemberEntity);
        return Result.ok();
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "memberCache")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "id") final String id, @RequestBody final TableMemberEntity tableMemberEntity) {
        tableMemberEntity.setId(id);
        this.tableMemberRepository.save(tableMemberEntity);
        return Result.ok();
    }
}
