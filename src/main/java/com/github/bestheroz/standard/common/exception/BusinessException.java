package com.github.bestheroz.standard.common.exception;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.io.FileNotFoundException;
import java.net.BindException;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@Setter
@Getter
public class BusinessException extends RuntimeException {
  public static final BusinessException SUCCESS_NORMAL =
      new BusinessException(ExceptionCode.SUCCESS_NORMAL);
  public static final BusinessException ERROR_SYSTEM =
      new BusinessException(ExceptionCode.ERROR_SYSTEM);
  public static final BusinessException FAIL_INVALID_REQUEST =
      new BusinessException(ExceptionCode.FAIL_INVALID_REQUEST);
  public static final BusinessException FAIL_INVALID_PARAMETER =
      new BusinessException(ExceptionCode.FAIL_INVALID_PARAMETER);
  public static final BusinessException FAIL_TRY_LOGIN_FIRST =
      new BusinessException(ExceptionCode.FAIL_TRY_LOGIN_FIRST);
  public static final BusinessException FAIL_NO_DATA_SUCCESS =
      new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS);
  private static final long serialVersionUID = -6837907198565524472L;
  public boolean printedStack = false;
  private ApiResult apiResult = Result.ok().getBody();

  public BusinessException(final Throwable throwable) {
    this.setApiResult(throwable);
  }

  public BusinessException(final ExceptionCode exceptionCode) {
    super(exceptionCode.toString());
    this.setApiResult(exceptionCode);
  }

  private void setApiResult(final ExceptionCode exceptionCode) {
    this.apiResult = ApiResult.code(exceptionCode);
  }

  private void setApiResult(final Throwable throwable) {
    this.apiResult = ApiResult.code(this.getExceptionCode(throwable));
  }

  private ExceptionCode getExceptionCode(final Throwable e) {
    if (e instanceof UncategorizedSQLException || e instanceof BindException) {
      return ExceptionCode.FAIL_INVALID_PARAMETER;
    } else if (e instanceof HttpMediaTypeNotAcceptableException
        || e instanceof HttpMediaTypeNotSupportedException
        || e instanceof HttpRequestMethodNotSupportedException) {
      return ExceptionCode.FAIL_INVALID_REQUEST;
    } else if (e instanceof FileNotFoundException) {
      return ExceptionCode.ERROR_FILE_NOT_FOUND;
    }
    return ExceptionCode.ERROR_SYSTEM;
  }

  public boolean isEquals(final ExceptionCode exceptionCode) {
    return StringUtils.equals(
        (String) this.apiResult.get(ApiResult.CODE_KEY), exceptionCode.getCode());
  }

  public boolean isContains(final Set<ExceptionCode> sets) {
    return sets.stream().anyMatch(this::isEquals);
  }

  @Override
  public String toString() {
    return this.apiResult.toString();
  }
}
