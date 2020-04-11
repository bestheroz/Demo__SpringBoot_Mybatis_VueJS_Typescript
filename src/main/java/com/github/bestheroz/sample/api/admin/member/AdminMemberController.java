package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.entity.samplemembermst.TableSampleMemberMstRepository;
import com.github.bestheroz.sample.api.entity.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/sample/admin/member")
public class AdminMemberController {
    @Resource private TableSampleMemberMstRepository tableSampleMemberMstRepository;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMemberMstRepository.findAll());
    }

    @GetMapping(value = "{memberId}")
    public ResponseVO getOne(@PathVariable(value = "memberId") final String memberId) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMemberMstRepository.findById(memberId));
    }

    @PostMapping
    public ResponseVO insert(@Valid @RequestBody final TableSampleMemberMstVO tableSampleMemberMstVO) {
        this.tableSampleMemberMstRepository.save(tableSampleMemberMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{memberId}")
    public ResponseVO update(@PathVariable(value = "memberId") final String memberId, @Valid @RequestBody final TableSampleMemberMstVO tableSampleMemberMstVO) {
        tableSampleMemberMstVO.setMemberId(memberId);
        this.tableSampleMemberMstRepository.save(tableSampleMemberMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{memberId}")
    public ResponseVO delete(@PathVariable(value = "memberId") final String memberId) {
        this.tableSampleMemberMstRepository.deleteById(memberId);
        return ResponseVO.SUCCESS_NORMAL;
    }
}
