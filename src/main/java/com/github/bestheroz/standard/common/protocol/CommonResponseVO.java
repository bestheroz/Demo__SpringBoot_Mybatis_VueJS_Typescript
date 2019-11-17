package com.github.bestheroz.standard.common.protocol;

import com.google.gson.JsonElement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class CommonResponseVO implements Serializable {
    private transient static final long serialVersionUID = 646012808993720354L;
    @ApiModelProperty(value = "responseCode", position = 1, example = "S000", required = true)
    private String responseCode;
    @ApiModelProperty(value = "responseMessage", position = 2, example = "성공", required = true)
    private String responseMessage;
    @ApiModelProperty(value = "responseData", position = 3, dataType = "springfox.documentation.spring.web.json.Json")
    private JsonElement responseData;
    @ApiModelProperty(value = "additionalMessage", position = 4, example = "(Nullable)")
    private String additionalMessage;

    @ApiModelProperty(hidden = true)
    public boolean isSuccess() {
        return StringUtils.startsWith(this.responseCode, "S");
    }

    @ApiModelProperty(hidden = true)
    public boolean isFail() {
        return StringUtils.startsWith(this.responseCode, "F");
    }
}
