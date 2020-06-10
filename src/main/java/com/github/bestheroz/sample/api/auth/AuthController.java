package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.security.CurrentUser;
import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Resource private AuthService authService;
    @Resource private TableMemberRepository tableMemberRepository;

    @PostMapping(value = "/login")
    ResponseEntity<ApiResult> login(@RequestBody final TableMemberVO tableMemberVO) {
        return Result.ok(this.authService.login(tableMemberVO.getId(), tableMemberVO.getPassword()));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<ApiResult> getMyData(@CurrentUser final UserVO userVO) {
        return Result.ok(this.authService.getMyData(userVO));
    }

    @PostMapping(value = "/verify")
    ResponseEntity<ApiResult> verify(@RequestHeader(value = "Authorization", required = false) final String token) {
        final TableMemberVO loginVO = SessionUtils.getLoginVO();
        this.authService.verify(token, loginVO.getId());
        return Result.ok(loginVO);
    }

    @DeleteMapping(value = "/logout")
    public void logout() {
        final TableMemberVO loginVO = SessionUtils.getLoginVO();
        this.tableMemberRepository.updateTokenNull(loginVO.getId());
        SessionUtils.logout();
    }
}
