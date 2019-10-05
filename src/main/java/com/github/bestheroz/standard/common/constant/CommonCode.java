package com.github.bestheroz.standard.common.constant;

import com.github.bestheroz.standard.context.init.InitWebConstantContext;

public class CommonCode {
    public static final String CONTEXT_PATH = InitWebConstantContext.getContextPath();
    public static final String YES = "YES";
    public static final String NO = "NO";
    // CommonResponseException 에 정의된 필드와 같다.
    public static final String RESPONSE_CODE = "responseCode";
    public static final String RESPONSE_MESSAGE = "responseMessage";
    public static final String RESPONSE_DATA = "responseData";
    public static final String ADDITIONAL_MESSAGE = "additionalMessage";
    // SWAGGER API 공통 메세지
    public static final String SWAGGER_COMMON_200_MESSAGE =
            "{<br/>&nbsp;&nbsp;responseCode&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 응답코드(Text)<br/>&nbsp;&nbsp;responseMessage&nbsp;&nbsp; : 응답메세지(Text)<br/>&nbsp;&nbsp;responseData&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 응답데이터 (Json)<br/>&nbsp;&nbsp;additionalMessage : 추가메세지(Text)<br/>}";

    protected CommonCode() {
        throw new UnsupportedOperationException();
    }
}
