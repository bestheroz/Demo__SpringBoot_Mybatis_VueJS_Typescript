package com.github.bestheroz.sample.api.admin.code;

import com.github.bestheroz.sample.api.entity.code.TableCodeEntity;
import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/codes")
public class AdminCodeController {
    @Resource private TableCodeRepository tableCodeRepository;

    @GetMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> getItems(@PathVariable(value = "codeGroup") final String codeGroup) {
        return Result.ok(this.tableCodeRepository.getItemsByKey(TableCodeEntity.class, Map.of("codeGroup", codeGroup)));
    }

    @GetMapping(value = "{codeGroup}/{code}")
    ResponseEntity<ApiResult> getItem(@PathVariable(value = "codeGroup") final String codeGroup, @PathVariable(value = "code", required = false) final String code) {
        return Result.ok(this.tableCodeRepository.getItem(TableCodeEntity.class, Map.of("codeGroup", codeGroup, "code", code)));
    }

    @PostMapping(value = "{codeGroup}")
    public ResponseEntity<ApiResult> insert(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeEntity tableCodeEntity) {
        this.tableCodeRepository.insert(tableCodeEntity);
        return Result.ok();
    }

    @PatchMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "codeCache", key = "#codeGroup")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "codeGroup") final String codeGroup, @PathVariable(value = "code") final String code,
                                            @RequestBody final TableCodeEntity tableCodeEntity) {
        this.tableCodeRepository.update(tableCodeEntity, Map.of("codeGroup", codeGroup, "code", code));
        return Result.ok();
    }

    @DeleteMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "codeCache", key = "#codeGroup")
    public ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup,
                                            @PathVariable(value = "code") final String code) {
        this.tableCodeRepository.delete(TableCodeEntity.class, Map.of("codeGroup", codeGroup, "code", code));
        return Result.ok();
    }
}
