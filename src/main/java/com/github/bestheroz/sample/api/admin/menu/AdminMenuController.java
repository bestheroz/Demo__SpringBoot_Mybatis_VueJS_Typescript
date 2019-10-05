package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.admin.menu.request.InsertSampleMenuMstRequestVO;
import com.github.bestheroz.sample.api.admin.menu.request.UpdateSampleMenuMstRequestVO;
import com.github.bestheroz.sample.api.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.api.tablevo.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "메뉴 관리")
@RestController
@RequestMapping(value = "/sample/admin/menu")
public class AdminMenuController {
    @Resource
    private AdminMenuService adminMenuService;

    @ApiOperation(value = "메뉴 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleMenuMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping
    public CommonResponseVO getList() throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getList());
    }

    @ApiOperation(value = "메뉴 데이터 취득")
    @ApiResponses(value = {@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "menuId/{menuId}")
    public CommonResponseVO getVO(@ApiParam("메뉴 아이디") @PathVariable(value = "menuId", required = false) final Integer menuId) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getVO(menuId, null));
    }

    @ApiOperation(value = "메뉴 데이터 취득")
    @ApiResponses(value = {@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "menuName/{menuName}")
    public CommonResponseVO getVO(@ApiParam("메뉴 명") @PathVariable(value = "menuName", required = false) final String menuName) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getVO(null, menuName));
    }

    @ApiOperation(value = "메뉴 데이터 추가")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PostMapping
    public CommonResponseVO insert(@RequestBody final InsertSampleMenuMstRequestVO vo) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMenuMstVO.class);
        tableSampleMenuMstVO.setCreatedBy("insert");
        tableSampleMenuMstVO.setUpdatedBy("insert");
        this.adminMenuService.insert(tableSampleMenuMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "메뉴 데이터 수정")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @PatchMapping(value = "{menuId}")
    public CommonResponseVO update(@ApiParam("메뉴 아이디") @PathVariable(value = "menuId") final Integer menuId, @RequestBody final UpdateSampleMenuMstRequestVO vo) throws CommonException {
        final TableSampleMenuMstVO tableSampleMenuMstVO = MyMapperUtils.writeObjectAsObject(vo, TableSampleMenuMstVO.class);
        tableSampleMenuMstVO.setMenuId(menuId);
        tableSampleMenuMstVO.setUpdatedBy("update");
        this.adminMenuService.update(tableSampleMenuMstVO);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "메뉴 데이터 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @DeleteMapping(value = "{menuId}")
    public CommonResponseVO delete(@ApiParam("메뉴 아이디") @PathVariable(value = "menuId") final Integer menuId) throws CommonException {
        this.adminMenuService.delete(menuId);
        return MyResponseUtils.SUCCESS_NORMAL;
    }

    @ApiOperation(value = "부모 메뉴<menuId, menuName> 리스트 취득")
    @ApiResponses(value = {@ApiResponse(response = GetSampleMenuMstVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @GetMapping(value = "getMenuTypeG")
    public CommonResponseVO getMenuTypeG() throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.adminMenuService.getMenuTypeG());
    }
}
