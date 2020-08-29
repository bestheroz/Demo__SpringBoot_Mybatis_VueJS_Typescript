package com.github.bestheroz.standard.context.log;

import com.github.bestheroz.standard.common.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@Aspect
@Component
public class TraceLogger {
    private static final String STR_CLASS_METHOD = "{0}.{1}({2})";
    private static final String STR_START_EXECUTE_TIME = "{} START ....... Execute Time ....... : {}";
    private static final String STR_END_EXECUTE_TIME = "{} E N D ....... Execute Time ....... : {} - return Value({}) : {}";

    @Around("execution(!private * com.github.bestheroz..*Controller.*(..)) || execution(!private * com.github.bestheroz..*Service.*(..)) " +
            "|| execution(!private * com.github.bestheroz..*Repository.*(..)) || execution(!private * com.github.bestheroz..*DAO.*(..))")
    public Object writeLog(final ProceedingJoinPoint pjp) throws Throwable {
        final Object retVal;

        final Class<?> targetClass = pjp.getTarget().getClass();
        final String formatClassMethod = MessageFormat.format(STR_CLASS_METHOD,
                StringUtils.startsWith(targetClass.getSimpleName(), "$Proxy") ? targetClass.getInterfaces()[0].getSimpleName() : targetClass.getSimpleName(), pjp.getSignature().getName(),
                Arrays.stream(pjp.getArgs()).map(item -> item == null ? StringUtils.EMPTY : item.getClass().getSimpleName()).collect(Collectors.joining(", ")));
        try {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            log.info(STR_START_EXECUTE_TIME, formatClassMethod, stopWatch.toString());

            retVal = pjp.proceed();

            stopWatch.stop();
            log.info(STR_END_EXECUTE_TIME, formatClassMethod, stopWatch.toString(), ((MethodSignature) pjp.getSignature()).getReturnType().getSimpleName(),
                    StringUtils.abbreviate(StringUtils.defaultString(MapperUtils.toString(retVal), "null"), "--skip long text--", 1000));
        } catch (final Throwable e) {
            log.warn("{} -\n{}", formatClassMethod, ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return retVal;
    }

}
