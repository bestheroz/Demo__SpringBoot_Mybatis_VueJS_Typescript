package com.github.bestheroz.standard.common.variable;

import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("variable")
public class VariableController {
    @Value("${variable.app.title}")
    private String appTitle;

    @GetMapping("appTitle")
    public ResponseVO getAppTitle() {
        return ResponseVO.getSuccessResponseVO(this.appTitle);
    }
}
