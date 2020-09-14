package com.github.bestheroz.demo.api.admin.codegroup;

import com.github.bestheroz.demo.api.entity.codegroup.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.codegroup.TableCodeGroupRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/codeGroups")
public class AdminCodeGroupController {
    @Resource private TableCodeGroupRepository tableCodeGroupRepository;
    @Resource private AdminCodeGroupService adminCodeGroupService;

    @GetMapping
    ResponseEntity<ApiResult> getItems() {
        return Result.ok(this.tableCodeGroupRepository.getItems(TableCodeGroupEntity.class));
    }

    @GetMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> getItem(@PathVariable(value = "codeGroup", required = false) final String codeGroup) {
        return Result.ok(this.tableCodeGroupRepository.getItem(TableCodeGroupEntity.class, Map.of("codeGroup", codeGroup)));
    }

    @PostMapping
    ResponseEntity<ApiResult> post(@RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
        this.tableCodeGroupRepository.insert(tableCodeGroupEntity);
        return Result.created();
    }

    @PatchMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> patch(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
        this.tableCodeGroupRepository.update(tableCodeGroupEntity, Map.of("codeGroup", codeGroup));
        return Result.ok();
    }

    @DeleteMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup) {
        this.adminCodeGroupService.delete(codeGroup);
        return Result.ok();
    }

}
