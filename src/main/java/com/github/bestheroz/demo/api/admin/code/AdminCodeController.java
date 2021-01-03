package com.github.bestheroz.demo.api.admin.code;

import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/codes")
public class AdminCodeController {
  @Resource
  private TableCodeRepository tableCodeRepository;

  @GetMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> getItems(
    @PathVariable(value = "codeGroup") final String codeGroup
  ) {
    return Result.ok(
      this.tableCodeRepository.getItemsByKey(Map.of("codeGroup", codeGroup))
    );
  }

  @PostMapping(value = "{codeGroup}")
  public ResponseEntity<ApiResult> post(
    @PathVariable(value = "codeGroup") final String codeGroup,
    @RequestBody final TableCodeEntity tableCodeEntity
  ) {
    tableCodeEntity.setCodeGroup(codeGroup);
    this.tableCodeRepository.insert(tableCodeEntity);
    return Result.created();
  }

  @PutMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> put(
    @PathVariable(value = "codeGroup") final String codeGroup,
    @PathVariable(value = "code") final String code,
    @RequestBody final TableCodeEntity tableCodeEntity
  ) {
    this.tableCodeRepository.updateByKey(
      tableCodeEntity,
      Map.of("codeGroup", codeGroup, "code", code)
    );
    return Result.ok();
  }

  @DeleteMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> delete(
    @PathVariable(value = "codeGroup") final String codeGroup,
    @PathVariable(value = "code") final String code
  ) {
    this.tableCodeRepository.deleteByKey(
      Map.of("codeGroup", codeGroup, "code", code)
    );
    return Result.ok();
  }
}
