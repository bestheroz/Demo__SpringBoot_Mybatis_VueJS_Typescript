package com.github.bestheroz.demo.api.admin.code.group;

import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/code/groups")
public class AdminCodeGroupController {
  @Resource private TableCodeGroupRepository tableCodeGroupRepository;
  @Resource private AdminCodeGroupService adminCodeGroupService;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.tableCodeGroupRepository.getItems());
  }

  @PostMapping
  ResponseEntity<ApiResult> post(@RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
    this.tableCodeGroupRepository.insert(tableCodeGroupEntity);
    return Result.created();
  }

  @PutMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> put(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
    this.tableCodeGroupRepository.updateByKey(tableCodeGroupEntity, Map.of("codeGroup", codeGroup));
    return Result.ok();
  }

  @DeleteMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup) {
    this.adminCodeGroupService.delete(codeGroup);
    return Result.ok();
  }
}
