package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.sample.api.entity.member.TableMemberRepository;
import com.github.bestheroz.sample.api.entity.member.TableMemberVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Resource private AuthService authService;
    @Resource private TableMemberRepository tableMemberRepository;

    @PostMapping(value = "/login")
    public ResponseVO login(@RequestBody final TableMemberVO tableMemberVO) {
        return ResponseVO.getSuccessResponseVO(this.authService.login(tableMemberVO.getId(), tableMemberVO.getPassword()));
    }

    @PostMapping(value = "/verify")
    public ResponseVO verify(@RequestHeader(value = "Authorization", required = false) final String token) {
        final TableMemberVO loginVO = SessionUtils.getLoginVO();
        this.authService.verify(token, loginVO.getId());
        return ResponseVO.getSuccessResponseVO(loginVO);
    }

    @DeleteMapping(value = "/logout")
    public void logout() {
        final TableMemberVO loginVO = SessionUtils.getLoginVO();
        this.authService.logoutToken(loginVO.getToken(), loginVO.getId());
        this.tableMemberRepository.updateTokenNull(loginVO.getId());
        SessionUtils.logout();
    }
}
