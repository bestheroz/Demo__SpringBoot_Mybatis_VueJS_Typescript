package com.github.bestheroz.demo.api.sign.in;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
  ResponseEntity<ApiResult<TokenDTO>> signIn(@RequestBody @Valid final SignInDTO payload) {
    final TokenDTO tokenDTO =
        this.signInService.signIn(payload.getLoginId(), payload.getPassword());
    if (tokenDTO == null || StringUtils.isEmpty(tokenDTO.getAccessToken())) {
      throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN);
    }
    return Result.ok(tokenDTO);
  }

  @GetMapping(value = "/refresh-token")
  public ResponseEntity<ApiResult<TokenDTO>> refreshToken(
      @RequestHeader(value = "AuthorizationR") final String refreshToken) {
    return Result.ok(this.signInService.getNewToken(refreshToken));
  }
}
