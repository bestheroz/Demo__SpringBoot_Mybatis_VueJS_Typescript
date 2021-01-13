package com.github.bestheroz.standard.common.util;

import javax.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AccessBeanUtils {
  private static AccessBeanUtils instance;

  @Resource private ApplicationContext applicationContext;

  private AccessBeanUtils() {
    if (instance == null) {
      instance = this;
    }
  }

  public static <T> T getBean(final Class<T> clazz) {
    return instance.applicationContext.getBean(clazz);
  }
}
