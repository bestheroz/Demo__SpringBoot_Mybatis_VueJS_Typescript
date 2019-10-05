package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "권한")
@RestController
@RequestMapping(value="/sample/auth")
public class AuthController {
    // private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private AuthService authService;

    @ApiOperation(value = "로그인 시도")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PostMapping(value = "/login")
    public CommonResponseVO doLogin(@ApiParam("회원 아이디") @RequestParam(value = "memberId") final String memberId,
                                    @ApiParam("회원 비밀번호(SHA512 값)") @RequestParam(value = "memberPw") final String memberPw) throws CommonException {
        this.authService.doLogin(memberId, memberPw);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
