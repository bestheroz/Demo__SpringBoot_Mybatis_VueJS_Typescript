package com.github.bestheroz.sample.web.login;

import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "로그인")
@RestController
public class LoginController {
    // private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "로그인 시도")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/login/doLogin", method = RequestMethod.POST)
    public CommonResponseVO doLogin(@ApiParam("회원 아이디") @RequestParam(value = "memberId") final String memberId,
                                    @ApiParam("회원 비밀번호") @RequestParam(value = "memberPw") final String memberPw) throws CommonException {
        this.loginService.doLogin(memberId, memberPw);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
