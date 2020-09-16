package com.github.bestheroz.standard.common.response;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public class Result {

  private Result() {}

  public static ResponseEntity<ApiResult> created() { // TODO : created 적용좀 하자.
    return ResponseEntity.status(201).body(ApiResult.ok());
  }

  public static ResponseEntity<ApiResult> ok() {
    return ResponseEntity.ok(ApiResult.ok());
  }

  public static ResponseEntity<ApiResult> ok(final Object data) {
    if (data instanceof Optional) {
      return ((Optional<?>) data).map(o -> ResponseEntity.ok(ApiResult.ok(o)))
        .orElseGet(() -> ResponseEntity.ok(ApiResult.ok()));
    } else {
      return ResponseEntity.ok(ApiResult.ok(data));
    }
  }

  public static ResponseEntity<ApiResult> ok(
    final Object data,
    final Integer paginationTotalLength
  ) {
    Assert.notNull(
      paginationTotalLength,
      "Parameter `paginationTotalLength` must not be null"
    );
    return ResponseEntity.ok(ApiResult.ok(data, paginationTotalLength));
  }

  public static ResponseEntity<ApiResult> error() {
    return ResponseEntity.ok(ApiResult.code(ExceptionCode.ERROR_SYSTEM));
  }

  public static ResponseEntity<ApiResult> error(
    final ExceptionCode exceptionCode
  ) {
    Assert.notNull(exceptionCode, "Parameter `exceptionCode` must not be null");
    return ResponseEntity.ok(ApiResult.code(exceptionCode));
  }

  public static ResponseEntity<ApiResult> error(
    final BusinessException businessException
  ) {
    Assert.notNull(
      businessException,
      "Parameter `exceptionCode` must not be null"
    );
    return ResponseEntity.ok(businessException.getApiResult());
  }

  public static ResponseEntity<ApiResult> unauthenticated() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
