package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("code")
public class CodeController {
    @Resource private CodeService codeService;

    @GetMapping(value = "{groupCode}")
    public ResponseVO getCodeVOList(@PathVariable(value = "groupCode") final String groupCode) {
        return ResponseVO.getSuccessResponseVO(this.codeService.getCodeVOList(groupCode));
    }
}
