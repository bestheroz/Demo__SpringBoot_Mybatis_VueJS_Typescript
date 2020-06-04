package com.github.bestheroz.sample.api.admin.codegroup;

import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupRepository;
import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupVO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/codeGroups")
public class AdminCodeGroupController {
    @Resource private TableCodeGroupRepository tableCodeGroupRepository;
    @Resource private AdminCodeGroupService adminCodeGroupService;

    @GetMapping
    ResponseEntity<ApiResult> getList() {
        return Result.ok(this.tableCodeGroupRepository.findAll());
    }

    @GetMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> getOne(@PathVariable(value = "codeGroup", required = false) final String codeGroup) {
        return Result.ok(this.tableCodeGroupRepository.findById(codeGroup));
    }

    @PostMapping
    ResponseEntity<ApiResult> post(@RequestBody final TableCodeGroupVO tableCodeGroupVO) {
        this.tableCodeGroupRepository.save(tableCodeGroupVO);
        return Result.ok();
    }

    @PatchMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> patch(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeGroupVO tableCodeGroupVO) {
        tableCodeGroupVO.setCodeGroup(codeGroup);
        this.tableCodeGroupRepository.save(tableCodeGroupVO);
        return Result.ok();
    }

    @DeleteMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup) {
        this.adminCodeGroupService.delete(codeGroup);
        return Result.ok();
    }

}
