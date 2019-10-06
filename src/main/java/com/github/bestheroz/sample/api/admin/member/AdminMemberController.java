package com.github.bestheroz.sample.api.admin.member;

import com.github.bestheroz.sample.api.admin.member.request.InsertSampleMemberMstRequestVO;
import com.github.bestheroz.sample.api.admin.member.request.UpdateSampleMemberMstRequestVO;
import com.github.bestheroz.sample.api.admin.member.response.GetSampleMemberMstVOResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.exception.CommonExceptionCode;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "회원 관리")
@RestController
@RequestMapping(value = "/sample/admin/member")
public class AdminMemberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private AdminMemberService adminMemberService;

    @ApiOperation(value = "회원 데이터 취득")
    @ApiResponses({@ApiResponse(response = GetSampleMemberMstVOResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping
    public CommonResponseVO getList() throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMemberService.getList());
    }

    @ApiOperation(value = "회원 데이터 취득")
    @ApiResponses({@ApiResponse(response = GetSampleMemberMstVOResponseVO.class, code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "{memberId}")
    public CommonResponseVO getOne(@ApiParam(value = "회원 아이디") @PathVariable(value = "memberId") final String memberId) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMemberService.getOne(memberId));
    }

    @ApiOperation(value = "회원 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PostMapping
    public CommonResponseVO insert(@Valid @RequestBody final InsertSampleMemberMstRequestVO vo, final BindingResult bindingResult) throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).getJsonObject().toString());
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMemberMstVO.class);
        tableSampleMemberMstVO.setCreatedBy("insert");
        tableSampleMemberMstVO.setUpdatedBy("insert");
        this.adminMemberService.insert(tableSampleMemberMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "회원 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PatchMapping(value = "{memberId}")
    public CommonResponseVO update(@ApiParam(value = "회원 아이디", required = true) @PathVariable(value = "memberId") final String memberId, @RequestBody final UpdateSampleMemberMstRequestVO vo,
                                   final BindingResult bindingResult) throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).getJsonObject().toString());
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMemberMstVO.class);
        tableSampleMemberMstVO.setMemberId(memberId);
        tableSampleMemberMstVO.setUpdatedBy("update");
        this.adminMemberService.update(tableSampleMemberMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "회원 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @DeleteMapping(value = "{memberId}")
    public CommonResponseVO delete(@ApiParam(value = "회원 아이디", required = true) @PathVariable(value = "memberId") final String memberId) throws CommonException {
        this.adminMemberService.delete(memberId);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
