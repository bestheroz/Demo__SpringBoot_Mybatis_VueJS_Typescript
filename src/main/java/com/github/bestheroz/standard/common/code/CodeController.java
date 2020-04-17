package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.standard.common.response.ResponseVO;
import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("code")
public class CodeController {
    @Resource private CodeDAO codeDAO;

    @GetMapping(value = "{codeGroup}")
    public ResponseVO getCodeVOList(@PathVariable(value = "codeGroup") final String codeGroup) {
        return ResponseVO.getSuccessResponseVO(this.codeDAO.getCodeVOList(codeGroup, SessionUtils.getAttributeInteger("authority")));
    }
}
