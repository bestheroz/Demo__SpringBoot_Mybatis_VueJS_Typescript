package com.github.bestheroz.sample.web.admin.codedet;

import com.github.bestheroz.sample.web.admin.codedet.request.InsertSampleCodeDetRequestVO;
import com.github.bestheroz.sample.web.admin.codedet.request.UpdateSampleCodeDetRequestVO;
import com.github.bestheroz.sample.web.admin.codedet.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "코드 관리")
@RestController
@RequestMapping(value = "/sample/admin/codedet")
public class AdminCodeDetController {
    @Autowired
    private AdminCodeDetService adminValueLabelService;

    @ApiOperation(value = "상세 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleCodeDetVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value="{grcode}", method = RequestMethod.GET)
    public CommonResponseVO getList(@ApiParam("그룹 코드") @PathVariable(value = "grcode") final String grcode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminValueLabelService.getList(grcode));
    }

    @ApiOperation(value = "상세 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value="{grcode}/{code}", method = RequestMethod.GET)
    public CommonResponseVO getVO(@ApiParam("그룹 코드") @PathVariable(value = "grcode") final String grcode,
                                  @ApiParam("상세 코드") @PathVariable(value = "code", required = false) final String code) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminValueLabelService.getVO(grcode, code));
    }

    @ApiOperation(value = "상세 코드 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(method = RequestMethod.POST)
    public CommonResponseVO insert(final InsertSampleCodeDetRequestVO vo) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setRegMemberId("insert");
        this.adminValueLabelService.insert(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(method = RequestMethod.PUT)
    public CommonResponseVO update(final UpdateSampleCodeDetRequestVO vo) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setUpdMemberId("update");
        this.adminValueLabelService.update(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value="{grcode}/{code}", method = RequestMethod.DELETE)
    public CommonResponseVO delete(@ApiParam("그룹 코드") @PathVariable(value = "grcode") final String grcode,
                                   @ApiParam("상세 코드") @PathVariable(value = "code") final String code) throws CommonException {
        this.adminValueLabelService.delete(grcode, code);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
