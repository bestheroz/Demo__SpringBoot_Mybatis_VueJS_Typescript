package com.github.bestheroz.standard.common.exception;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
@RestController
public class ApiExceptionHandler {
  // 아래서 놓친 예외가 있을때 이곳으로 확인하기 위해 존재한다.
  // 놓친 예외는 이곳에서 확인하여 추가해주면 된다.
  @ExceptionHandler({Throwable.class})
  public ResponseEntity<ApiResult> exception(final Throwable e) {
    log.warn(ExceptionUtils.getStackTrace(e));
    return Result.error();
  }

  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<ApiResult> businessException(final BusinessException e) {
    if (e.isEquals(ExceptionCode.FAIL_TRY_LOGIN_FIRST)) {
      return Result.unauthenticated();
    }
    return Result.error(e);
  }

  @ExceptionHandler({UsernameNotFoundException.class})
  public ResponseEntity<ApiResult> usernameNotFoundException(final UsernameNotFoundException e) {
    return Result.unauthenticated();
  }

  @ExceptionHandler({
    BindException.class,
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class
  })
  public ResponseEntity<ApiResult> bindException(final Throwable e) {
    log.warn(ExceptionUtils.getStackTrace(e));
    return Result.error(BusinessException.FAIL_INVALID_PARAMETER);
  }

  @ExceptionHandler({
    HttpMediaTypeNotAcceptableException.class,
    HttpMediaTypeNotSupportedException.class,
    HttpRequestMethodNotSupportedException.class,
    HttpClientErrorException.class
  })
  public ResponseEntity<ApiResult> httpMediaTypeNotAcceptableException(
      final Throwable e, final HttpServletResponse response) {
    if (StringUtils.equals(
        response.getHeader("refreshToken"), "must")) { // 데이터 수정시 가끔 이곳으로 넘어와 버리네..
      return Result.unauthenticated();
    }
    log.warn(ExceptionUtils.getStackTrace(e));
    return Result.error(BusinessException.FAIL_INVALID_REQUEST);
  }

  @ExceptionHandler({MultipartException.class})
  public ResponseEntity<ApiResult> multipartException(final MultipartException e) {
    log.warn(ExceptionUtils.getStackTrace(e));
    final ResponseEntity<ApiResult> result;
    //        if
    // (ExceptionUtils.getMessage(e).contains(FileUploadBase.SizeLimitExceededException.class.getSimpleName())) {
    //            result = new Response(ExceptionCode.FAIL_FILE_SIZE).getJsonObject();
    //        } else {
    result = Result.error();
    //        }
    return result;
  }

  @ExceptionHandler({DuplicateKeyException.class})
  public ResponseEntity<ApiResult> duplicateKeyException(final DuplicateKeyException e) {
    log.warn(ExceptionUtils.getStackTrace(e));
    return Result.error(ExceptionCode.FAIL_UNIQUE_CONSTRAINT_VIOLATED);
  }
}
