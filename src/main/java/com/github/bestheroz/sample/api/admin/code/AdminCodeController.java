package com.github.bestheroz.sample.api.admin.code;

import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.sample.api.entity.code.TableCodeVO;
import com.github.bestheroz.sample.api.entity.code.TableCodeVOId;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/codes")
public class AdminCodeController {
    @Resource private TableCodeRepository tableCodeRepository;

    @GetMapping(value = "{codeGroup}")
    ResponseEntity<ApiResult> getList(@PathVariable(value = "codeGroup") final String codeGroup) {
        return Result.ok(this.tableCodeRepository.findByCodeGroup(codeGroup));
    }

    @GetMapping(value = "{codeGroup}/{code}")
    ResponseEntity<ApiResult> getOne(@PathVariable(value = "codeGroup") final String codeGroup, @PathVariable(value = "code", required = false) final String code) {
        return Result.ok(this.tableCodeRepository.findById(new TableCodeVOId(codeGroup, code)));
    }

    @PostMapping(value = "{codeGroup}")
    @CacheEvict(value = "codeCache", key = "#codeGroup")
    public ResponseEntity<ApiResult> insert(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeVO tableCodeVO) {
        tableCodeVO.setCodeGroup(codeGroup);
        this.tableCodeRepository.save(tableCodeVO);
        return Result.ok();
    }

    @PatchMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "codeCache", key = "#codeGroup")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "codeGroup") final String codeGroup,
                                            @PathVariable(value = "code") final String code, @RequestBody final TableCodeVO tableCodeVO) {
        tableCodeVO.setCodeGroup(codeGroup);
        tableCodeVO.setCode(code);
        this.tableCodeRepository.save(tableCodeVO);
        return Result.ok();
    }

    @DeleteMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "codeCache", key = "#codeGroup")
    public ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup,
                                            @PathVariable(value = "code") final String code) {
        this.tableCodeRepository.deleteById(new TableCodeVOId(codeGroup, code));
        return Result.ok();
    }
}
