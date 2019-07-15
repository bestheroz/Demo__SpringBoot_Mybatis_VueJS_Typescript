package com.github.bestheroz.sample.web.admin.valuelabel;

import com.github.bestheroz.sample.web.admin.valuelabel.request.InsertSampleCodeDetRequestVO;
import com.github.bestheroz.sample.web.admin.valuelabel.request.InsertSampleCodeMstRequestVO;
import com.github.bestheroz.sample.web.admin.valuelabel.request.UpdateSampleCodeDetRequestVO;
import com.github.bestheroz.sample.web.admin.valuelabel.request.UpdateSampleCodeMstRequestVO;
import com.github.bestheroz.sample.web.admin.valuelabel.response.GetSampleCodeDetVOListResponseVO;
import com.github.bestheroz.sample.web.admin.valuelabel.response.GetSampleCodeMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplecodedet.TableSampleCodeDetVO;
import com.github.bestheroz.sample.web.tablevo.samplecodemst.TableSampleCodeMstVO;
import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "코드 관리")
@RestController
public class AdminValueLabelController {
    @Autowired
    private AdminValueLabelService adminValueLabelService;

    @ApiOperation(value = "그룹 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleCodeMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/getSampleCodeMstVOList", method = RequestMethod.POST)
    public CommonResponseVO getSampleCodeMstVOList(@ApiParam("그룹 코드") @RequestParam(value = "grcode", required = false) final String grcode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminValueLabelService.getSampleCodeMstVOList(grcode));
    }

    @ApiOperation(value = "그룹 코드 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/insertSampleCodeMst", method = RequestMethod.POST)
    public CommonResponseVO insertSampleCodeMst(final InsertSampleCodeMstRequestVO vo) throws CommonException {
        final TableSampleCodeMstVO tableSampleCodeMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeMstVO.class);
        tableSampleCodeMstVO.setRegMemberId("insertSampleCodeMst");
        this.adminValueLabelService.insertSampleCodeMst(tableSampleCodeMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "그룹 코드 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/updateSampleCodeMst", method = RequestMethod.POST)
    public CommonResponseVO updateSampleCodeMst(final UpdateSampleCodeMstRequestVO vo) throws CommonException {
        final TableSampleCodeMstVO tableSampleCodeMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeMstVO.class);
        tableSampleCodeMstVO.setUpdMemberId("updateSampleCodeMst");
        this.adminValueLabelService.updateSampleCodeMst(tableSampleCodeMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleCodeDetVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/getSampleCodeDetVOList", method = RequestMethod.POST)
    public CommonResponseVO getSampleCodeDetVOList(@ApiParam("그룹 코드") @RequestParam(value = "grcode") final String grcode,
                                                   @ApiParam("상세 코드") @RequestParam(value = "code", required = false) final String code) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminValueLabelService.getSampleCodeDetVOList(grcode, code));
    }

    @ApiOperation(value = "상세 코드 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/insertSampleCodeDet", method = RequestMethod.POST)
    public CommonResponseVO insertSampleCodeDet(final InsertSampleCodeDetRequestVO vo) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setRegMemberId("insertSampleCodeDet");
        this.adminValueLabelService.insertSampleCodeDet(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/updateSampleCodeDet", method = RequestMethod.POST)
    public CommonResponseVO updateSampleCodeDet(final UpdateSampleCodeDetRequestVO vo) throws CommonException {
        final TableSampleCodeDetVO tableSampleCodeDetVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleCodeDetVO.class);
        tableSampleCodeDetVO.setUpdMemberId("updateSampleCodeDet");
        this.adminValueLabelService.updateSampleCodeDet(tableSampleCodeDetVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "그룹 코드 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/deleteCOMM_CODE", method = RequestMethod.POST)
    public CommonResponseVO deleteCOMM_CODE(@ApiParam("그룹 코드") @RequestParam(value = "grcode") final String grcode) throws CommonException {
        this.adminValueLabelService.deleteCOMM_CODE(grcode);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "상세 코드 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/sample/admin/valuelabel/deleteSampleCodeDet", method = RequestMethod.POST)
    public CommonResponseVO deleteSampleCodeDet(@ApiParam("그룹 코드") @RequestParam(value = "grcode") final String grcode,
                                                @ApiParam("상세 코드") @RequestParam(value = "code") final String code) throws CommonException {
        this.adminValueLabelService.deleteSampleCodeDet(grcode, code);
        return MyResponseUtils.SUCCESS_NORMAL;
    }
}
