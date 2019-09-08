package com.github.bestheroz.standard.common.valuelabel;

import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "<value, label> 코드")
@RestController
@RequestMapping(value = "/common/valuelabel")
public class ValueLabelController {
    @Resource
    private ValueLabelService valueLabelService;

    @ApiOperation(value = "<value, label> 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetValueLabeVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "{grcode}", method = RequestMethod.GET)
    public CommonResponseVO get(@ApiParam(value = "그룹 코드") @PathVariable(value = "grcode") final String grcode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.valueLabelService.getList(grcode));
    }
}
