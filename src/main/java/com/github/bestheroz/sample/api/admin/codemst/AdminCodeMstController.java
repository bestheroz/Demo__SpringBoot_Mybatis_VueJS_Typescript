package com.github.bestheroz.sample.api.admin.codemst;

import com.github.bestheroz.sample.api.admin.codemst.request.InsertSampleCodeMstRequestVO;
import com.github.bestheroz.sample.api.admin.codemst.request.UpdateSampleCodeMstRequestVO;
import com.github.bestheroz.sample.api.admin.codemst.response.GetSampleCodeMstVOListResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplecodemst.TableSampleCodeMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.exception.CommonExceptionCode;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import com.github.bestheroz.standard.context.swagger.SwaggerConfig;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "코드 관리")
@RestController
@RequestMapping(value = "/sample/admin/codemst")
public class AdminCodeMstController {

    @Resource
    private AdminCodeMstService adminCodeMstService;

    @ApiOperation(value = "그룹 코드 데이터 리스트 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleCodeMstVOListResponseVO.class, responseContainer = "List", code = 200, message = SwaggerConfig.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping
    public CommonResponseVO getList() throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminCodeMstService.getList());
    }

    @ApiOperation(value = "그룹 코드 데이터 취득")
    @ApiResponses({@ApiResponse(response = TableSampleCodeMstVO.class, code = 200, message = SwaggerConfig.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "{groupCode}")
    public CommonResponseVO get(@ApiParam("그룹 코드") @PathVariable(value = "groupCode", required = false) final String groupCode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminCodeMstService.getOne(groupCode));
    }

    @ApiOperation(value = "그룹 코드 데이터 추가")
    @PostMapping
    public CommonResponseVO post(@RequestBody final InsertSampleCodeMstRequestVO vo, final BindingResult bindingResult) throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).toString());
        }
        final TableSampleCodeMstVO tableSampleCodeMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeMstVO.class);
        tableSampleCodeMstVO.setCreatedBy("insert");
        tableSampleCodeMstVO.setUpdatedBy("insert");
        this.adminCodeMstService.insert(tableSampleCodeMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "그룹 코드 데이터 수정")
    @PatchMapping(value = "{groupCode}")
    public CommonResponseVO patch(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode, @RequestBody final UpdateSampleCodeMstRequestVO vo, final BindingResult bindingResult)
            throws CommonException {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            log.warn(new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER, fieldError.getDefaultMessage()).toString());
        }
        final TableSampleCodeMstVO tableSampleCodeMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeMstVO.class);
        tableSampleCodeMstVO.setGroupCode(groupCode);
        tableSampleCodeMstVO.setUpdatedBy("update");
        this.adminCodeMstService.update(tableSampleCodeMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "그룹 코드 데이터 삭제")
    @DeleteMapping(value = "{groupCode}")
    public CommonResponseVO delete(@ApiParam("그룹 코드") @PathVariable(value = "groupCode") final String groupCode) throws CommonException {
        this.adminCodeMstService.delete(groupCode);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

}
