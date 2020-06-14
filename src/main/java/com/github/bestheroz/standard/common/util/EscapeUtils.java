package com.github.bestheroz.standard.common.util;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.nhncorp.lucy.security.xss.XssSaxFilter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class EscapeUtils {
    private final XssSaxFilter XSS_SAX_FILTER = XssSaxFilter.getInstance("lucy-xss-sax.xml");

    public String escapeAll(final String dirty) {
        return StringUtils.isNotEmpty(dirty) ? escapeXss(unescapeAll(dirty)) : dirty;
    }

    private String escapeXss(final String dirty) {
        return XssPreventer.escape(XssPreventer.unescape(XSS_SAX_FILTER.doFilter(dirty)));
    }

    public String unescapeAll(final String clean) {
        return StringUtils.isNotEmpty(clean) ? unescapeXss(clean) : clean;
    }

    private String unescapeXss(final String clean) {
        return XssPreventer.unescape(clean);
    }
}
