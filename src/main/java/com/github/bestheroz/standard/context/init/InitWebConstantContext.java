package com.github.bestheroz.standard.context.init;

import com.github.bestheroz.standard.common.util.MyDateUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.util.TimeZone;

@Configuration
public class InitWebConstantContext {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String contextPath = null;

    @Autowired
    public void setConstant(final ServletContext servletContext) throws IllegalArgumentException {
        DateTimeZone.setDefault(MyDateUtils.TIME_ZONE_ASIA_SEOUL);
        TimeZone.setDefault(MyDateUtils.TIME_ZONE_ASIA_SEOUL.toTimeZone());
        servletContext.setAttribute("TIME_ZONE_ASIA_SEOUL", MyDateUtils.TIME_ZONE_ASIA_SEOUL.getID());
        servletContext.setAttribute("LOCALE_KOREAN", MyDateUtils.LOCALE_KOREAN.toString());
        this.logger.info("DateTimeZone/TimeZone.setDefault(\"{}\"); - Complete", MyDateUtils.TIME_ZONE_ASIA_SEOUL.getID());
    }

    public static String getContextPath() {
        return contextPath;
    }
}
