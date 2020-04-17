package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/members")
public class AdminMemberController {
    @Resource private TableMemberRepository tableMemberRepository;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.tableMemberRepository.findAll());
    }

    @GetMapping(value = "{id}")
    public ResponseVO getOne(@PathVariable(value = "id") final String id) {
        return ResponseVO.getSuccessResponseVO(this.tableMemberRepository.findById(id));
    }

    @PostMapping
    @CacheEvict(value = "getMemberList")
    public ResponseVO insert(@RequestBody final TableMemberVO tableMemberVO) {
        this.tableMemberRepository.save(tableMemberVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "getMemberList")
    public ResponseVO update(@PathVariable(value = "id") final String id, @RequestBody final TableMemberVO tableMemberVO) {
        tableMemberVO.setId(id);
        this.tableMemberRepository.save(tableMemberVO);
        return ResponseVO.SUCCESS_NORMAL;
    }
}
