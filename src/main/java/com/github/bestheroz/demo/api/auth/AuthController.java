package com.github.bestheroz.demo.api.auth;

import com.github.bestheroz.demo.api.entity.member.TableMemberEntity;
import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

  @Resource
  private AuthService authService;

  @Resource
  private TableMemberRepository tableMemberRepository;

  @PostMapping(value = "/login")
  @ResponseBody
  ResponseEntity<ApiResult> login(
    @RequestBody final TableMemberEntity tableMemberEntity
  ) {
    return Result.ok(
      this.authService.login(
          tableMemberEntity.getId(),
          tableMemberEntity.getPassword()
        )
    );
  }

  @GetMapping(value = "/me")
  public ResponseEntity<ApiResult> getMine(
    @RequestHeader(value = "Authorization") final String token
  ) {
    return Result.ok(
      this.tableMemberRepository.getItemByKey(
          Map.of("id", JwtTokenProvider.getUserPk(token))
        )
        .orElseThrow(() -> BusinessException.FAIL_TRY_LOGIN_FIRST)
    );
  }

  @GetMapping(value = "/refreshToken")
  public ResponseEntity<ApiResult> refreshToken(
    @RequestHeader(value = "AuthorizationR") final String refreshToken
  ) {
    return Result.ok(this.authService.getNewAccessToken(refreshToken));
  }

  @PostMapping(value = "/initPassword")
  ResponseEntity<ApiResult> initPassword(
    @RequestBody final TableMemberEntity tableMemberEntity
  ) {
    this.authService.initPassword(
        tableMemberEntity.getId(),
        tableMemberEntity.getPassword()
      );
    return Result.ok();
  }

  @DeleteMapping(value = "/logout")
  public void logout() {
    this.authService.logout();
    AuthenticationUtils.logout();
  }
}
