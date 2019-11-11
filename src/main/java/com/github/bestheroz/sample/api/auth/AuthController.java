package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.sample.api.auth.request.LoginRequestVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "권한")
@RestController
@RequestMapping(value = "/sample/auth")
@Slf4j
public class AuthController {
    @Resource
    private AuthService authService;

    @ApiOperation(value = "로그인 시도")
    @PostMapping(value = "/login")
    public CommonResponseVO doLogin(@RequestBody final LoginRequestVO requestVO) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.authService.login(requestVO.getMemberId(), requestVO.getMemberPw()));
    }

    @ApiOperation(value = "토큰 검증")
    @PostMapping(value = "/verify")
    public CommonResponseVO verify(@ApiParam("Token") @RequestHeader(value = "Authorization", required = false) final String token) throws CommonException {
        log.debug(token);
        this.authService.verify(token);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
