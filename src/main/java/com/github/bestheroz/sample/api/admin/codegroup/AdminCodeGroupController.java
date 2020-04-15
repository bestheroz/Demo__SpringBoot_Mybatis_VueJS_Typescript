package com.github.bestheroz.sample.api.admin.codegroup;

import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupRepository;
import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/codeGroups")
public class AdminCodeGroupController {
    @Resource private TableCodeGroupRepository tableCodeGroupRepository;
    @Resource private AdminCodeGroupService adminCodeGroupService;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.tableCodeGroupRepository.findAll());
    }

    @GetMapping(value = "{codeGroup}")
    public ResponseVO getOne(@PathVariable(value = "codeGroup", required = false) final String codeGroup) {
        return ResponseVO.getSuccessResponseVO(this.tableCodeGroupRepository.findById(codeGroup));
    }

    @PostMapping
    public ResponseVO post(@RequestBody final TableCodeGroupVO tableCodeGroupVO) {
        this.tableCodeGroupRepository.save(tableCodeGroupVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{codeGroup}")
    public ResponseVO patch(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeGroupVO tableCodeGroupVO) {
        tableCodeGroupVO.setCodeGroup(codeGroup);
        this.tableCodeGroupRepository.save(tableCodeGroupVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{codeGroup}")
    public ResponseVO delete(@PathVariable(value = "codeGroup") final String codeGroup) {
        this.adminCodeGroupService.delete(codeGroup);
        return ResponseVO.SUCCESS_NORMAL;
    }

}
