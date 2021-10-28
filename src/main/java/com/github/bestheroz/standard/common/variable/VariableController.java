package com.github.bestheroz.standard.common.variable;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Map;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/variables")
@ConfigurationProperties(prefix = "variable")
@Setter
public class VariableController {
  private Map<String, String> app;

  @GetMapping("title")
  public ResponseEntity<ApiResult<String>> getTitle() {
    return Result.ok(this.app.get("title"));
  }
}
