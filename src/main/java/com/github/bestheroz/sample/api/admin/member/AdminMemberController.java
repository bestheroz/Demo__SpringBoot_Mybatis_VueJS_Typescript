package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/members")
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
    @CacheEvict(value = "getMemberList")
    public ResponseEntity<ApiResult> insert(@RequestBody final TableMemberVO tableMemberVO) {
        this.tableMemberRepository.save(tableMemberVO);
        return Result.ok();
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "getMemberList")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "id") final String id, @RequestBody final TableMemberVO tableMemberVO) {
        tableMemberVO.setId(id);
        this.tableMemberRepository.save(tableMemberVO);
        return Result.ok();
    }
}
