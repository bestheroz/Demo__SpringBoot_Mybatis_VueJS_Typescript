package com.github.bestheroz.sample.api.admin.codedet;

import com.github.bestheroz.sample.api.admin.codedet.request.InsertSampleCodeDetRequestVO;
import com.github.bestheroz.sample.api.admin.codedet.request.UpdateSampleCodeDetRequestVO;
import com.github.bestheroz.sample.api.admin.codedet.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplecodedet.TableSampleCodeDetVO;
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

@Api(tags = "코드 관리")
@RestController
@RequestMapping(value = "/sample/admin/codedet")
public class AdminCodeDetController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AdminCodeDetService adminCodeDetService;

    @ApiOperation(value = "상세 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleCodeDetVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "{groupCode}")
    public CommonResponseVO getList(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminCodeDetService.getList(groupCode));
    }

    @ApiOperation(value = "상세 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "{groupCode}/{code}")
    public CommonResponseVO getOne(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode,
                                   @ApiParam("상세 코드") @PathVariable(value = "code", required = false) final String code) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminCodeDetService.getOne(groupCode, code));
    }

    @ApiOperation(value = "상세 코드 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PostMapping
    public CommonResponseVO insert(@RequestBody final InsertSampleCodeDetRequestVO vo, final BindingResult bindingResult) throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).getJsonObject().toString());
        }
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setCreatedBy("insert");
        tableSampleCodeDetVO.setUpdatedBy("insert");
        this.adminCodeDetService.insert(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PatchMapping(value = "{groupCode}/{code}")
    public CommonResponseVO update(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode,
                                   @ApiParam("상세 코드") @PathVariable(value = "code") final String code, @RequestBody final UpdateSampleCodeDetRequestVO vo, final BindingResult bindingResult)
            throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            this.logger.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).getJsonObject().toString());
        }
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setGroupCode(groupCode);
        tableSampleCodeDetVO.setCode(code);
        tableSampleCodeDetVO.setUpdatedBy("update");
        this.adminCodeDetService.update(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @DeleteMapping(value = "{groupCode}/{code}")
    public CommonResponseVO delete(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode,
                                   @ApiParam("상세 코드") @PathVariable(value = "code") final String code) throws CommonException {
        this.adminCodeDetService.delete(groupCode, code);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
