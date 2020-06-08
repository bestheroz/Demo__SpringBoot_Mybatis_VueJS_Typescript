package com.github.bestheroz.standard.common.response;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public class Result {
    private Result() {
    }

    public static ResponseEntity<ApiResult> created() {
        return ResponseEntity.status(201).body(ApiResult.ok());
    }

    public static ResponseEntity<ApiResult> ok() {
        return ResponseEntity.ok(ApiResult.ok());
    }

    public static ResponseEntity<ApiResult> ok(final Object data) {
        return ResponseEntity.ok(ApiResult.ok(data));
    }

    public static ResponseEntity<ApiResult> ok(final Object data, final Integer paginationTotalLength) {
        Assert.notNull(paginationTotalLength, "Parameter `paginationTotalLength` must not be null");
        return ResponseEntity.ok(ApiResult.ok(data, paginationTotalLength));
    }

    public static ResponseEntity<ApiResult> error() {
        return ResponseEntity.status(500).body(ApiResult.code(ExceptionCode.FAIL_SYSTEM_ERROR));
    }

    public static ResponseEntity<ApiResult> error(final ExceptionCode exceptionCode) {
        Assert.notNull(exceptionCode, "Parameter `exceptionCode` must not be null");
        if (StringUtils.startsWith(exceptionCode.getCode(), "F")) {
            return ResponseEntity.badRequest().body(ApiResult.code(exceptionCode));
        } else {
            return ResponseEntity.status(500).body(ApiResult.code(exceptionCode));
        }
    }

    public static ResponseEntity<ApiResult> error(final BusinessException businessException) {
        Assert.notNull(businessException, "Parameter `exceptionCode` must not be null");
        final ApiResult apiResult = businessException.getApiResult();
        if (StringUtils.startsWith((String) apiResult.get(ApiResult.CODE_KEY), "F")) {
            return ResponseEntity.badRequest().body(apiResult);
        } else {
            return ResponseEntity.status(500).body(apiResult);
        }
    }

    public static ResponseEntity<ApiResult> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<ApiResult> unauthenticated() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public static ResponseEntity<ApiResult> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
