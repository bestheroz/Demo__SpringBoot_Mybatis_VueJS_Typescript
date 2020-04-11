package com.github.bestheroz.standard.common.util;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.nhncorp.lucy.security.xss.XssSaxFilter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class EscapeUtils {
    private static final XssSaxFilter XSS_SAX_FILTER = XssSaxFilter.getInstance("lucy-xss-sax.xml");

    public static String escapeAll(final String dirty) {
        return StringUtils.isNotEmpty(dirty) ? escapeXss(unescapeAll(dirty)) : dirty;
    }

    private static String escapeXss(final String dirty) {
        return XssPreventer.escape(XssPreventer.unescape(XSS_SAX_FILTER.doFilter(dirty)));
    }

    public static String unescapeAll(final String clean) {
        return StringUtils.isNotEmpty(clean) ? unescapeXss(clean) : clean;
    }

    private static String unescapeXss(final String clean) {
        return XssPreventer.unescape(clean);
    }
}
