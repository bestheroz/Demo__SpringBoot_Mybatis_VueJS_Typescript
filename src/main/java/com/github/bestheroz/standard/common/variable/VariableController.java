package com.github.bestheroz.standard.common.variable;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("variable")
public class VariableController {
    @Value("${variable.app.title}")
    private String appTitle;

    @GetMapping("appTitle")
    ResponseEntity<ApiResult> getAppTitle() {
        return Result.ok(this.appTitle);
    }
}
