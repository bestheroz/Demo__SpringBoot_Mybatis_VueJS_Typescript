package com.github.bestheroz.standard.context.log;

import com.github.bestheroz.standard.common.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@Aspect
@Component
public class TraceLogger {
  private static final String STR_START_EXECUTE_TIME = "{} START .......";
  private static final String STR_END_EXECUTE_TIME = "{} E N D [{}ms] - return: {}";
  private static final String STR_END_EXECUTE_TIME_FOR_EXCEPTION = "{} THROW [{}ms]";

  @Around(
      "execution(!private * com.github.bestheroz..*Controller.*(..)) || execution(!private * com.github.bestheroz..*Service.*(..)) "
          + "|| execution(!private * com.github.bestheroz..*Repository.*(..))")
  public Object writeLog(final ProceedingJoinPoint pjp) throws Throwable {
    final Object retVal;

    final String signature =
        StringUtils.remove(
            pjp.getStaticPart().getSignature().toString(),
            pjp.getStaticPart().getSignature().getDeclaringType().getPackageName().concat("."));
    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      log.info(STR_START_EXECUTE_TIME, signature);

      retVal = pjp.proceed();

      stopWatch.stop();
      final String str = MapperUtils.toString(retVal);
      log.info(
          STR_END_EXECUTE_TIME,
          signature,
          stopWatch.getTime(),
          StringUtils.abbreviate(
              StringUtils.defaultString(str, "null"),
              "--skip massive text-- total length : " + StringUtils.length(str),
              1000));
    } catch (final Throwable e) {
      if (stopWatch.isStarted()) {
        stopWatch.stop();
      }
      log.info(STR_END_EXECUTE_TIME_FOR_EXCEPTION, signature, stopWatch.getTime());
      throw e;
    }
    return retVal;
  }
}
