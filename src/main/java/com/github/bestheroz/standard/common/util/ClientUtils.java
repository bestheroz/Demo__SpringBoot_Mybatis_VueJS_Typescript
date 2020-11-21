package com.github.bestheroz.standard.common.util;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@UtilityClass
public class ClientUtils {

  public String getClientIpAddr(final HttpServletRequest request) {
    Assert.notNull(request, "Parameter `request` must not be null");

    final String[] headerValues = {
      "X-FORWARDED-FOR",
      "X-Forwarded-For",
      "Proxy-Client-IP",
      "WL-Proxy-Client-IP",
      "HTTP_CLIENT_IP",
      "HTTP_X_FORWARDED_FOR"
    };
    return Arrays
      .stream(headerValues)
      .filter(
        headerValue -> {
          final String ip = request.getHeader(headerValue);
          return (
            StringUtils.isNotEmpty(ip) &&
            !StringUtils.equalsIgnoreCase(ip, "unknown")
          );
        }
      )
      .findFirst()
      .orElseGet(request::getRemoteAddr);
  }
}
