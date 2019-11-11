package com.github.bestheroz.standard.common.exception;

import com.github.bestheroz.standard.common.util.MyMapperUtils;
import com.github.bestheroz.standard.common.util.MyNullUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Slf4j
@SuppressWarnings("serial")
public class CommonException extends RuntimeException {
    public static final CommonException EXCEPTION_SUCCESS_NORMAL = new CommonException(CommonExceptionCode.SUCCESS_NORMAL);
    public static final CommonException EXCEPTION_FAIL_SYSTEM = new CommonException(CommonExceptionCode.FAIL_SYSTEM_ERROR);
    public static final CommonException EXCEPTION_FAIL_INVALID_REQUEST = new CommonException(CommonExceptionCode.FAIL_INVALID_REQUEST);
    public static final CommonException EXCEPTION_FAIL_INVALID_PARAMETER = new CommonException(CommonExceptionCode.FAIL_INVALID_PARAMETER);
    public static final CommonException EXCEPTION_FAIL_NO_DATA_SUCCESS = new CommonException(CommonExceptionCode.FAIL_NO_DATA_SUCCESS);
    private String responseCode;
    private String responseMessage;
    private JsonElement responseData;
    private String additionalMessage;

    public CommonException(final Exception exception) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(exception));
        this.setReturnValue(this.extractResultCode(exception.getClass().getSimpleName()), exception.getMessage(), null);
    }

    public CommonException(final Exception exception, final Object data) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(exception));
        this.setReturnValue(this.extractResultCode(exception.getClass().getSimpleName()), exception.getMessage(), data);
    }

    public CommonException(final DataAccessException dataAccessException) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(dataAccessException));
        this.setReturnValue(this.extractResultCode(dataAccessException.getClass().getSimpleName()), dataAccessException.getMessage(), null);
    }

    public CommonException(final DataAccessException dataAccessException, final Object data) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(dataAccessException));
        this.setReturnValue(this.extractResultCode(dataAccessException.getClass().getSimpleName()), dataAccessException.getMessage(), data);
    }

    public CommonException(final Throwable throwable) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(throwable));
        this.setReturnValue(this.extractResultCode(throwable.getClass().getSimpleName()), throwable.getMessage(), null);
    }

    public CommonException(final Throwable throwable, final Object data) {
        log.warn("Received error message: {}", ExceptionUtils.getStackTrace(throwable));
        this.setReturnValue(this.extractResultCode(throwable.getClass().getSimpleName()), throwable.getMessage(), data);
    }

    public CommonException(final CommonExceptionCode commonExceptionCode) {
        this.setReturnValue(commonExceptionCode, null, null);
    }

    public CommonException(final CommonExceptionCode commonExceptionCode, final Object data) {
        this.setReturnValue(commonExceptionCode, null, data);
    }

    public CommonException(final CommonExceptionCode commonExceptionCode, final Object data, final String additionalMessage) {
        this.setReturnValue(commonExceptionCode, additionalMessage, data);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public JsonObject getJsonObject() {
        if (StringUtils.isNotEmpty(this.responseCode)) {
            final JsonObject result = new JsonObject();
            result.addProperty("responseCode", this.responseCode);
            result.addProperty("responseMessage", this.responseMessage);
            if (MyNullUtils.isNotEmpty(this.responseData)) {
                result.add("responseData", MyMapperUtils.writeObjectAsJsonElement(this.responseData));
            }
            if (StringUtils.isNotEmpty(this.additionalMessage)) {
                result.addProperty("additionalMessage", this.additionalMessage);
            }
            return result;
        } else {
            return EXCEPTION_FAIL_SYSTEM.getJsonObject();
        }
    }

    private CommonExceptionCode extractResultCode(final String exceptionName) {
        CommonExceptionCode commonExceptionCode = CommonExceptionCode.FAIL_SYSTEM_ERROR;
        switch (exceptionName) {
            case "UncategorizedSQLException":
            case "BindException":
                commonExceptionCode = CommonExceptionCode.FAIL_INVALID_PARAMETER;
                break;
            case "HttpMediaTypeNotAcceptableException":
            case "HttpMediaTypeNotSupportedException":
            case "HttpRequestMethodNotSupportedException":
                commonExceptionCode = CommonExceptionCode.FAIL_INVALID_REQUEST;
                break;
            case "FileNotFoundException":
                commonExceptionCode = CommonExceptionCode.FAIL_FILE_NOT_FOUND;
                break;
            default:
                break;
        }
        log.warn("{}: return value => {}", exceptionName, commonExceptionCode.getCode());
        return commonExceptionCode;
    }

    private void setReturnValue(final CommonExceptionCode commonExceptionCode, final String additionalMessage, final Object data) {
        this.responseCode = commonExceptionCode.getCode();
        this.responseMessage = commonExceptionCode.getMessage();
        this.additionalMessage = additionalMessage;
        this.responseData = MyMapperUtils.writeObjectAsJsonElement(data);
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public String getAdditionalMessage() {
        return this.additionalMessage;
    }

    public JsonElement getResponseData() {
        return this.responseData;
    }

    public <T> T getResponseData(final Class<T> valueType) {
        return MyMapperUtils.writeObjectAsObject(this.responseData, valueType);
    }

    public boolean isEquals(final CommonExceptionCode commonExceptionCode) {
        return StringUtils.equals(this.getResponseCode(), commonExceptionCode.getCode());
    }

    public boolean isContains(final List<CommonExceptionCode> list) {
        final boolean result = false;
        for (final CommonExceptionCode commonExceptionCode : list) {
            if (this.isEquals(commonExceptionCode)) {
                return true;
            }
        }
        return result;
    }

    public boolean isExceptionNoDataSuccesss() {
        return this.isEquals(CommonExceptionCode.FAIL_NO_DATA_SUCCESS);
    }

    public boolean isSuccess() {
        return StringUtils.startsWith(this.responseCode, "S");
    }

    public boolean isFail() {
        return StringUtils.startsWith(this.responseCode, "F");
    }

    @Override
    public String getMessage() {
        return this.responseMessage;
    }

    @Override
    public String toString() {
        return this.getJsonObject().toString();
    }
}
