package com.github.bestheroz.standard.common.valuelabel;

import com.github.bestheroz.standard.common.constant.CommonCode;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.protocol.CommonResponseVO;
import com.github.bestheroz.standard.common.util.MyResponseUtils;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "<value, label> 코드")
@RestController
public class ValueLabelController {
    @Autowired
    private ValueLabelService valueLabelService;

    @ApiOperation(value = "<value, label> 데이터 취득")
    @ApiResponses(value = {@ApiResponse(response = GetValueLabeVOListResponseVO.class, responseContainer = "List", code = 200, message = CommonCode.SWAGGER_COMMON_200_MESSAGE)})
    @RequestMapping(value = "/common/valuelabel/getValueLabeVOList", method = RequestMethod.POST)
    public CommonResponseVO getValueLabeVOList(@ApiParam(value = "그룹 코드", required = true) @RequestParam(value = "grcode") final String grcode) throws CommonException {
        return MyResponseUtils.getSuccessCommonResponseVO(this.valueLabelService.getValueLabeVOList(grcode));
    }
}
