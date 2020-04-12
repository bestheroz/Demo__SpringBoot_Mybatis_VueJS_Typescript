package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.sample.api.entity.samplemembermst.TableSampleMemberMstVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Slf4j
public class SessionUtils {
    public static final String SESSION_VALUE_OF_LOGIN_VO = "loginVO";
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

    protected SessionUtils() {
        throw new UnsupportedOperationException();
    }

    public static HttpSession getSession() {
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }

    public static boolean isLoggedIn() {
        return getSession().getAttribute(SESSION_VALUE_OF_LOGIN_VO) != null;
    }

    public static boolean isNotLoggedIn() {
        return !isLoggedIn();
    }

    public static TableSampleMemberMstVO getLoginVO() {
        try {
            final TableSampleMemberMstVO loginVO = (TableSampleMemberMstVO) getSession().getAttribute(SESSION_VALUE_OF_LOGIN_VO);
            if (loginVO == null) {
                log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
                throw BusinessException.FAIL_TRY_LOGIN_FIRST;
            }
            return loginVO;
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public static void setLoginVO(final TableSampleMemberMstVO loginVO) {
        getSession().setAttribute(SESSION_VALUE_OF_LOGIN_VO, loginVO);
        getSession().setAttribute("id", loginVO.getId());
        getSession().setAttribute("name", loginVO.getName());
    }

    public static String getId() {
        try {
            final String id = (String) getSession().getAttribute("id");
            if (id == null) {
                log.warn(BusinessException.FAIL_TRY_LOGIN_FIRST.toString());
                throw BusinessException.FAIL_TRY_LOGIN_FIRST;
            }
            return id;
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public static void logout() {
        getSession().invalidate();
    }

    public static boolean hasAttribute(final String name) {
        try {
            return getSession().getAttribute(name) != null;
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static String getAttribute(final String name) {
        try {
            return (String) getSession().getAttribute(name);
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static void setAttribute(final String name, final Object value) {
        getSession().setAttribute(name, value);
    }

    public static Integer getAttributeInteger(final String name) {
        try {
            return (Integer) getSession().getAttribute(name);
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static Object getAttributeObject(final String name) {
        try {
            return getSession().getAttribute(name);
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttributeObject(final String name, final Class<T> returnType) {
        try {
            return (T) getSession().getAttribute(name);
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static void removeAttribute(final String name) {
        getSession().removeAttribute(name);
    }

    public static void printAttributeList() {
        final JsonObject result = new JsonObject();
        try {
            for (final Enumeration<String> attributeNames = getSession().getAttributeNames();
                 attributeNames.hasMoreElements(); ) {
                final String nextElement = attributeNames.nextElement();
                result.addProperty(nextElement, MapperUtils.toString(getSession().getAttribute(nextElement)));
            }
            LOGGER.debug(result.toString());
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    public static JsonObject getAttributeJson(final HttpSession session) {
        final JsonObject result = new JsonObject();
        try {
            for (final Enumeration<String> attributeNames = session.getAttributeNames();
                 attributeNames.hasMoreElements(); ) {
                final String nextElement = attributeNames.nextElement();
                result.addProperty(nextElement, MapperUtils.toString(session.getAttribute(nextElement)));
            }
        } catch (final Throwable e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }
}
