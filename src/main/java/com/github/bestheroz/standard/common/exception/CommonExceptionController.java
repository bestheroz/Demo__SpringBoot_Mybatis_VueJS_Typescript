package com.github.bestheroz.standard.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@RestController
public class CommonExceptionController {
    @RequestMapping(value = {"/common/exception/error"})
    public Json errorView(final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return new Json(new CommonException(e).toString());
    }
}
