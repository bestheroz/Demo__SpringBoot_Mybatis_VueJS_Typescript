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

    @GetMapping(value = "{id}")
    public ResponseVO getOne(@PathVariable(value = "id") final String id) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMemberMstRepository.findById(id));
    }

    @PostMapping
    public ResponseVO insert(@Valid @RequestBody final TableSampleMemberMstVO tableSampleMemberMstVO) {
        this.tableSampleMemberMstRepository.save(tableSampleMemberMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{id}")
    public ResponseVO update(@PathVariable(value = "id") final String id, @Valid @RequestBody final TableSampleMemberMstVO tableSampleMemberMstVO) {
        tableSampleMemberMstVO.setId(id);
        this.tableSampleMemberMstRepository.save(tableSampleMemberMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{id}")
    public ResponseVO delete(@PathVariable(value = "id") final String id) {
        this.tableSampleMemberMstRepository.deleteById(id);
        return ResponseVO.SUCCESS_NORMAL;
    }
}
