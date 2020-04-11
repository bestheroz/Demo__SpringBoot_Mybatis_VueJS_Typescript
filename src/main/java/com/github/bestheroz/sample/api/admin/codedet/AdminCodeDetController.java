package com.github.bestheroz.sample.api.admin.codedet;

import com.github.bestheroz.sample.api.entity.samplecodedet.TableSampleCodeDetId;
import com.github.bestheroz.sample.api.entity.samplecodedet.TableSampleCodeDetRepository;
import com.github.bestheroz.sample.api.entity.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/sample/admin/codedet")
public class AdminCodeDetController {
    @Resource private TableSampleCodeDetRepository tableSampleCodeDetRepository;

    @GetMapping(value = "{groupCode}")
    public ResponseVO getList(@PathVariable(value = "groupCode") final String groupCode) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleCodeDetRepository.findByGroupCode(groupCode));
    }

    @GetMapping(value = "{groupCode}/{code}")
    public ResponseVO getOne(@PathVariable(value = "groupCode") final String groupCode, @PathVariable(value = "code", required = false) final String code) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleCodeDetRepository.findById(new TableSampleCodeDetId(groupCode, code)));
    }

    @PostMapping(value = "{groupCode}/{code}")
    public ResponseVO insert(@RequestBody final TableSampleCodeDetVO tableSampleCodeDetVO) {
        this.tableSampleCodeDetRepository.save(tableSampleCodeDetVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{groupCode}/{code}")
    public ResponseVO update(@PathVariable(value = "groupCode") final String groupCode,
                             @PathVariable(value = "code") final String code, @RequestBody final TableSampleCodeDetVO tableSampleCodeDetVO) {
        tableSampleCodeDetVO.setGroupCode(groupCode);
        tableSampleCodeDetVO.setCode(code);
        this.tableSampleCodeDetRepository.save(tableSampleCodeDetVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{groupCode}/{code}")
    public ResponseVO delete(@PathVariable(value = "groupCode") final String groupCode,
                             @PathVariable(value = "code") final String code) {
        this.tableSampleCodeDetRepository.deleteById(new TableSampleCodeDetId(groupCode, code));
        return ResponseVO.SUCCESS_NORMAL;
    }
}
