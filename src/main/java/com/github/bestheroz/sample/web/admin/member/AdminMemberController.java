package com.github.bestheroz.sample.web.admin.member;

import com.github.bestheroz.sample.web.admin.member.request.InsertSampleMemberMstRequestVO;
import com.github.bestheroz.sample.web.admin.member.request.UpdateSampleMemberMstRequestVO;
import com.github.bestheroz.sample.web.admin.member.response.GetSampleMemberMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.exception.CommonExceptionCode;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "회원 관리")
@RestController
public class AdminMemberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AdminMemberService adminMemberService;

    @ApiOperation(value = "회원 데이터 취득")
    @ApiResponses({@ApiResponse(response = GetSampleMemberMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/member/getSampleMemberMstVOList", method = RequestMethod.POST)
    public CommonResponseVO getSampleMemberMstVOList(@ApiParam(value = "회원 아이디") @RequestParam(value = "memberId", required = false) final String memberId) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMemberService.getSampleMemberMstVOList(memberId));
    }

    @ApiOperation(value = "회원 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/member/insertSampleMemberMst", method = RequestMethod.POST)
    public CommonResponseVO insertSampleMemberMst(@Valid final InsertSampleMemberMstRequestVO vo, final BindingResult bindingResult) throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            this.logger.warn(new CommonException(CommonExceptionCode.ERROR_INVALID_PARAMETER, fieldError.getDefaultMessage()).getJsonObject().toString());
        }
        final TableSampleMemberMstVO tableSampleMemberMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMemberMstVO.class);
        if (tableSampleMemberMstVO.getMemberId().equals("22")) {
            throw new CommonException(CommonExceptionCode.ERROR_INVALID_PARAMETER);
        }
        tableSampleMemberMstVO.setRegMemberId("insertSampleMemberMst");
        this.adminMemberService.insertSampleMemberMst(tableSampleMemberMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "회원 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/member/updateSampleMemberMst", method = RequestMethod.POST)
    public CommonResponseVO updateSampleMemberMst(final UpdateSampleMemberMstRequestVO vo) throws CommonException {
        final TableSampleMemberMstVO tableSampleMemberMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMemberMstVO.class);
        tableSampleMemberMstVO.setUpdMemberId("updateSampleMemberMst");
        this.adminMemberService.updateSampleMemberMst(tableSampleMemberMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "회원 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/member/deleteSampleMemberMst", method = RequestMethod.POST)
    public CommonResponseVO deleteSampleMemberMst(@ApiParam(value = "회원 아이디", required = true) @RequestParam(value = "memberId") final String memberId) throws CommonException {
        this.adminMemberService.deleteSampleMemberMst(memberId);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
