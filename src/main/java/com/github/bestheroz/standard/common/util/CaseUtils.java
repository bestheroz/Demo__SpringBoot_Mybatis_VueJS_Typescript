package com.github.bestheroz.standard.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CaseUtils {
  public String getCamelCaseToSnakeCase(final String str) {
    final StringBuilder result = new StringBuilder(str.length() * 2);
    result.append(Character.toLowerCase(str.charAt(0)));
    for (int i = 1; i < str.length(); i++) {
      final char ch = str.charAt(i);
      if (Character.isUpperCase(ch)) {
        result.append('_').append(Character.toLowerCase(ch));
      } else {
        result.append(ch);
      }
    }
    return result.toString();
  }

  public String getSnakeCaseToCamelCase(final String str) {
    final StringBuilder result = new StringBuilder(str.length());
    boolean beforeUnderbar = false;
    for (int i = 0; i < str.length(); i++) {
      final char ch = str.charAt(i);
      if (ch == '_') {
        beforeUnderbar = true;
      } else {
        result.append(beforeUnderbar ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        beforeUnderbar = false;
      }
    }
    return result.toString();
  }
}
