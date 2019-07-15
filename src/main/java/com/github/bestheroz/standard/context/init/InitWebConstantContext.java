package com.github.bestheroz.standard.context.init;

import com.github.bestheroz.standard.common.util.MyDateUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired(required = false)
    public void setConstant(final ServletContext servletContext) throws IllegalArgumentException {
        this.setContextPath(servletContext);

        // final Field[] fields = CommonCode.class.getDeclaredFields();
        // for (final Field field : fields) {
        // // this.logger.info("servletContext.setAttribute(\"{}\", \"{}\");", field.getName(), field.get(field.getName()));
        // servletContext.setAttribute(field.getName(), field.get(field.getName()));
        // }
        // this.logger.info("CommonCode - Complete");

        // final CommonResultCode[] values = CommonResultCode.values();
        // for (final CommonResultCode value : values) {
        // // this.logger.info("servletContext.setAttribute(\"{}\", \"{}\");", value.name(), value.getCode());
        // servletContext.setAttribute(value.name(), value.getCode());
        // }
        // this.logger.info("CommonResultCode - Complete");

        DateTimeZone.setDefault(MyDateUtils.TIME_ZONE_ASIA_SEOUL);
        TimeZone.setDefault(MyDateUtils.TIME_ZONE_ASIA_SEOUL.toTimeZone());
        servletContext.setAttribute("TIME_ZONE_ASIA_SEOUL", MyDateUtils.TIME_ZONE_ASIA_SEOUL.getID());
        servletContext.setAttribute("LOCALE_KOREAN", MyDateUtils.LOCALE_KOREAN.toString());
        this.logger.info("DateTimeZone/TimeZone.setDefault(\"{}\"); - Complete", MyDateUtils.TIME_ZONE_ASIA_SEOUL.getID());
    }

    private void setContextPath(final ServletContext servletContext) {
        if (contextPath == null) {
            String tempContextPath = servletContext.getContextPath();
            if (StringUtils.equals(tempContextPath, "/")) {
                tempContextPath = "";
            }
            if (StringUtils.isNotEmpty(tempContextPath) && StringUtils.endsWith(tempContextPath, "/")) {
                tempContextPath = StringUtils.removeEnd(tempContextPath, "/");
            }
            contextPath = tempContextPath;
        }
        servletContext.setAttribute("CONTEXT_PATH", contextPath);
        this.logger.info("servletContext.setAttribute(\"CONTEXT_PATH\", \"{}\");", contextPath);
    }

    public static String getContextPath() {
        return contextPath;
    }
}
