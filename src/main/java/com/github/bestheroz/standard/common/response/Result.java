package com.github.bestheroz.standard.common.response;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public class Result {

  private Result() {}

  public static ResponseEntity<ApiResult<?>> created() {
    return ResponseEntity.status(201).body(ApiResult.of(ExceptionCode.SUCCESS_NORMAL));
  }

  public static <T> ResponseEntity<ApiResult<T>> created(final T data) {
    return ResponseEntity.status(201).body(ApiResult.of(ExceptionCode.SUCCESS_NORMAL, data));
  }

  public static ResponseEntity<ApiResult<?>> ok() {
    return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS_NORMAL));
  }

  public static <T> ResponseEntity<ApiResult<T>> ok(final T data) {
    return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS_NORMAL, data));
  }

  public static ResponseEntity<ApiResult<?>> error() {
    return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.ERROR_SYSTEM));
  }

  public static ResponseEntity<ApiResult<?>> error(final ExceptionCode exceptionCode) {
    Assert.notNull(exceptionCode, "Parameter `exceptionCode` must not be null");
    return ResponseEntity.status(200).body(ApiResult.of(exceptionCode));
  }

  public static ResponseEntity<ApiResult<?>> error(final BusinessException businessException) {
    Assert.notNull(businessException, "Parameter `exceptionCode` must not be null");
    return ResponseEntity.status(200)
        .body(
            ApiResult.builder()
                .code(businessException.getApiResult().getCode())
                .message(businessException.getApiResult().getMessage())
                .build());
  }

  public static ResponseEntity<ApiResult<?>> unauthenticated() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
