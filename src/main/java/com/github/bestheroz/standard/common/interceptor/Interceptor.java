package com.github.bestheroz.standard.common.interceptor;

import com.github.bestheroz.sample.api.auth.AuthService;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.util.MyAccessBeanUtils;
import com.github.bestheroz.standard.common.util.MyMapperUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class Interceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Interceptor.class);
    private static final String REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP = "{} ....... Request Complete Execute Time ....... : {}";
    private static final String STR_STOP_WATCH = "mi.stopWatch";

    @Override
    // preHandle : controller 이벤트 호출전
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws CommonException {
        LOGGER.debug(MessageFormat.format("[{0}]{1}", request.getMethod(), new UrlPathHelper().getPathWithinApplication(request)));
        if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
            try {
                MyAccessBeanUtils.getBean(AuthService.class).verify(request.getHeader("Authorization"));
            } catch (final CommonException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                try {
                    response.getWriter().write(MyMapperUtils.writeObjectAsString(e.getJsonObject()));
                } catch (final IOException ex) {
                    LOGGER.warn(ExceptionUtils.getStackTrace(ex));
                }
                return false;
            }
        }
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request.setAttribute(STR_STOP_WATCH, stopWatch);
        return true;
    }

    @Override
    // postHandle : controller 호출 후 view 페이지 출력전
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {
    }

    @Override
    // afterCompletion : controller + view 페이지 모두 출력 후
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        final StopWatch stopWatch = (StopWatch) request.getAttribute(STR_STOP_WATCH);
        stopWatch.stop();
        LOGGER.info(REQUEST_COMPLETE_EXECUTE_TIME_INCLUDE_JSP, new UrlPathHelper().getPathWithinApplication(request), stopWatch.toString());
        request.removeAttribute(STR_STOP_WATCH);
    }

    @Override
    // afterConcurrentHandlingStarted: 뭐 동시에 핸들링 해주는 메서드인대 정확히는 모르겠습니다
    public void afterConcurrentHandlingStarted(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        //
    }
}
