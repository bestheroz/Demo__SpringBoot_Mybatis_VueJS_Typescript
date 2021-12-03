package com.github.bestheroz.demo.api.sign.in;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sign-in")
@RequiredArgsConstructor
public class SignInController {
  private final SignInService signInService;

  @PostMapping
  ResponseEntity<ApiResult<Map<String, String>>> signIn(
      @RequestBody @Valid final SignInDTO payload) {
    return Result.ok(this.signInService.signIn(payload.getLoginId(), payload.getPassword()));
  }

  @GetMapping(value = "/refresh-token")
  public ResponseEntity<ApiResult<String>> refreshToken(
      @RequestHeader(value = "AuthorizationR") final String refreshToken) {
    return Result.ok(this.signInService.getNewAccessToken(refreshToken));
  }
}
