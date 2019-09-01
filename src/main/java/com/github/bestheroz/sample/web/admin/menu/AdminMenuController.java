package com.github.bestheroz.sample.web.admin.menu;

import com.github.bestheroz.sample.web.admin.menu.request.InsertSampleMenuMstRequestVO;
import com.github.bestheroz.sample.web.admin.menu.request.UpdateSampleMenuMstRequestVO;
import com.github.bestheroz.sample.web.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstVO;
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

@Api(tags = "메뉴 관리")
@RestController
@RequestMapping(value = "/sample/admin/menu")
public class AdminMenuController {
    @Autowired
    private AdminMenuService adminMenuService;

    @ApiOperation(value = "메뉴 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleMenuMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(method = RequestMethod.GET)
    public CommonResponseVO getList(@ApiParam("메뉴 아이디") @RequestParam(value = "menuId", required = false) final Integer menuId,
                                    @ApiParam("메뉴 명") @RequestParam(value = "menuNm", required = false) final String menuNm) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getList(menuId, menuNm));
    }

    @ApiOperation(value = "메뉴 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(method = RequestMethod.POST)
    public CommonResponseVO insert(final InsertSampleMenuMstRequestVO vo) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMenuMstVO.class);
        tableSampleMenuMstVO.setRegMemberId("insert");
        this.adminMenuService.insert(tableSampleMenuMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "메뉴 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping( method = RequestMethod.PUT)
    public CommonResponseVO update(final UpdateSampleMenuMstRequestVO vo) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMenuMstVO.class);
        tableSampleMenuMstVO.setUpdMemberId("update");
        this.adminMenuService.update(tableSampleMenuMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "메뉴 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "{menuId}", method = RequestMethod.POST)
    public CommonResponseVO delete(@ApiParam("메뉴 아이디") @RequestParam(value = "menuId") final Integer menuId) throws CommonException {
        this.adminMenuService.delete(menuId);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "부모 메뉴<menuId, menuNm> 리스트 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleMenuMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "getMenuTypG", method = RequestMethod.POST)
    public CommonResponseVO getMenuTypG() throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getMenuTypG());
    }
}
