package com.github.bestheroz.standard.common.variable;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@RequestMapping("api/variables")
@ConfigurationProperties(prefix = "app")
@Setter
public class VariableController {
    private String title;

    @GetMapping("title")
    ResponseEntity<ApiResult> getAppTitle() {
        return Result.ok(this.title);
    }
}
