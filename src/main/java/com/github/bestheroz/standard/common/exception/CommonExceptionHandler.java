package com.github.bestheroz.standard.common.exception;

import com.google.gson.stream.MalformedJsonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    // 아래서 놓친 예외가 있을때 이곳으로 확인하기 위해 존재한다.
    // 놓친 예외는 이곳에서 확인하여 추가해주면 된다.
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public Json exception(final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(CommonException.EXCEPTION_FAIL_SYSTEM.toString());
    }

    @ExceptionHandler({CommonException.class})
    @ResponseBody
    public Json commonResponseException(final CommonException e) {
        log.warn(e.toString());
        return new Json(e.toString());
    }

    @ExceptionHandler({BindException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public Json bindException(final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(CommonException.EXCEPTION_FAIL_INVALID_PARAMETER.toString());
    }

    @ExceptionHandler(value = {ServletRequestBindingException.class})
    @ResponseBody
    public Json servletRequestBindingException(final ServletRequestBindingException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(CommonException.EXCEPTION_FAIL_INVALID_REQUEST.toString());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MalformedJsonException.class})
    @ResponseBody
    public Json dataConvertException(final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(CommonException.EXCEPTION_FAIL_INVALID_REQUEST.toString());
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class, HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class, HttpClientErrorException.class})
    @ResponseBody
    public Json httpMediaTypeNotAcceptableException(final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(CommonException.EXCEPTION_FAIL_INVALID_REQUEST.toString());
    }
}
