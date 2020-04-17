package com.github.bestheroz.sample.api.admin.code;

import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.sample.api.entity.code.TableCodeVO;
import com.github.bestheroz.sample.api.entity.code.TableCodeVOId;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/admin/codes")
public class AdminCodeController {
    @Resource private TableCodeRepository tableCodeRepository;

    @GetMapping(value = "{codeGroup}")
    public ResponseVO getList(@PathVariable(value = "codeGroup") final String codeGroup) {
        return ResponseVO.getSuccessResponseVO(this.tableCodeRepository.findByCodeGroup(codeGroup));
    }

    @GetMapping(value = "{codeGroup}/{code}")
    public ResponseVO getOne(@PathVariable(value = "codeGroup") final String codeGroup, @PathVariable(value = "code", required = false) final String code) {
        return ResponseVO.getSuccessResponseVO(this.tableCodeRepository.findById(new TableCodeVOId(codeGroup, code)));
    }

    @PostMapping(value = "{codeGroup}")
    @CacheEvict(value = "CodeVO", key = "#codeGroup")
    public ResponseVO insert(@PathVariable(value = "codeGroup") final String codeGroup, @RequestBody final TableCodeVO tableCodeVO) {
        tableCodeVO.setCodeGroup(codeGroup);
        this.tableCodeRepository.save(tableCodeVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "CodeVO", key = "#codeGroup")
    public ResponseVO update(@PathVariable(value = "codeGroup") final String codeGroup,
                             @PathVariable(value = "code") final String code, @RequestBody final TableCodeVO tableCodeVO) {
        tableCodeVO.setCodeGroup(codeGroup);
        tableCodeVO.setCode(code);
        this.tableCodeRepository.save(tableCodeVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{codeGroup}/{code}")
    @CacheEvict(value = "CodeVO", key = "#codeGroup")
    public ResponseVO delete(@PathVariable(value = "codeGroup") final String codeGroup,
                             @PathVariable(value = "code") final String code) {
        this.tableCodeRepository.deleteById(new TableCodeVOId(codeGroup, code));
        return ResponseVO.SUCCESS_NORMAL;
    }
}
