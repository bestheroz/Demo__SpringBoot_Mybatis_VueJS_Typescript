package com.github.bestheroz.standard.context.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.github.bestheroz.standard.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

public class LoggingLayout extends LayoutBase<ILoggingEvent> {
    private static final String TOTAL_LENGTH = "Total length : ";
    private static final String STR_SKIP_TOO_LONG_TEXT = "<!-- skip too long text -->";

    @Override
    public String doLayout(final ILoggingEvent event) {
        final StringBuffer sbuf = new StringBuffer(1024);

        if (StringUtils.endsWithAny(event.getLoggerName(), "TraceLoggingInAOP", "JwtAuthenticationFilter")) {
            if (StringUtils.equals(event.getLevel().levelStr, "WARN") && event.getCallerData()[0].getLineNumber() == 49) {
                this.getExceptionMessage(event, sbuf);
            } else {
                this.getMessageOnlyFormattedMessage(event, sbuf);
            }
        } else if (StringUtils.startsWithAny(event.getLoggerName(), "org.jdbcdslog.StatementLogger", "org.jdbcdslog.ResultSetLogger")) {
            if (StringUtils.startsWithAny(event.getFormattedMessage(), "java.sql.ResultSet.next")) {
                this.getMessageOnlyFormattedMessage(event, sbuf);
            } else {
                this.getSql(event, sbuf);
            }
        } else if (StringUtils.endsWithAny(event.getLoggerName(), "RequestResponseBodyMethodProcessor")) {
            this.getMessageStartWithMethod(event, sbuf);
        } else if (StringUtils.equalsAny(event.getLoggerName(), "org.hibernate.type.descriptor.sql.BasicBinder")) {
            this.getMessageOnlyFormattedMessage(event, sbuf);
        } else {
            this.getMessageByStdPattern(event, sbuf);
        }

        return sbuf.toString();
    }

    private void getMessageByStdPattern(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(event.getLoggerName());
        sbuf.append(".");
        sbuf.append(event.getCallerData()[0].getMethodName());
        sbuf.append("(");
        sbuf.append(event.getCallerData()[0].getLineNumber());
        sbuf.append(")");
        sbuf.append(" === ");
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageStartWithMethod(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(event.getCallerData()[0].getMethodName());
        sbuf.append(" === ");
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getExceptionMessage(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append("<[Throw Exception]>");
        sbuf.append(event.getFormattedMessage());
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageOnlyFormattedMessage(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getSql(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(CoreConstants.LINE_SEPARATOR).append(StringUtils.abbreviate(event.getFormattedMessage(), 20000)).append(CoreConstants.LINE_SEPARATOR).append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageHeader(final ILoggingEvent event, final StringBuffer sbuf) {
        sbuf.append(DateUtils.getString(event.getTimeStamp(), "HH:mm:ss.SSS"));
        sbuf.append(" [");
        sbuf.append(StringUtils.rightPad(event.getLevel().levelStr, 5));
        sbuf.append("] ");
    }

    private String skipLogText(final String formattedMessage) {
        String logString = formattedMessage;
        if (StringUtils.isNotEmpty(logString)) {
            // 필더되지 않은 값을 ...(보통 request)
            String[] substringsBetween = StringUtils.substringsBetween(logString, "src=\\\"data:image/", "\">");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            // 필터된 값을... (보통 response)
            substringsBetween = StringUtils.substringsBetween(logString, "src=&quot;data:image/", "&quot;&gt;");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            // sql 로그에서..
            substringsBetween = StringUtils.substringsBetween(logString, "src='||chr(38)||'quot;data:image/", "chr(38)||'quot;'||chr(38)||'gt;'");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            if (logString.length() > 20000) {
                logString = StringUtils.abbreviate(logString, STR_SKIP_TOO_LONG_TEXT, 20000).concat(TOTAL_LENGTH).concat(String.valueOf(logString.length()));
            }
        }
        return logString;
    }
}
