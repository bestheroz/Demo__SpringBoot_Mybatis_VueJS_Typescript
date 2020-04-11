package com.github.bestheroz.sample.api.admin.codemst;

import com.github.bestheroz.sample.api.entity.samplecodemst.TableSampleCodeMstRepository;
import com.github.bestheroz.sample.api.entity.samplecodemst.TableSampleCodeMstVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/sample/admin/codemst")
public class AdminCodeMstController {
    @Resource private TableSampleCodeMstRepository tableSampleCodeMstRepository;
    @Resource private AdminCodeMstService adminCodeMstService;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.tableSampleCodeMstRepository.findAll());
    }

    @GetMapping(value = "{groupCode}")
    public ResponseVO getOne(@PathVariable(value = "groupCode", required = false) final String groupCode) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleCodeMstRepository.findById(groupCode));
    }

    @PostMapping
    public ResponseVO post(@RequestBody final TableSampleCodeMstVO tableSampleCodeMstVO) {
        this.tableSampleCodeMstRepository.save(tableSampleCodeMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{groupCode}")
    public ResponseVO patch(@PathVariable(value = "groupCode") final String groupCode, @RequestBody final TableSampleCodeMstVO tableSampleCodeMstVO) {
        tableSampleCodeMstVO.setGroupCode(groupCode);
        this.tableSampleCodeMstRepository.save(tableSampleCodeMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{groupCode}")
    public ResponseVO delete(@PathVariable(value = "groupCode") final String groupCode) {
        this.adminCodeMstService.delete(groupCode);
        return ResponseVO.SUCCESS_NORMAL;
    }

}
