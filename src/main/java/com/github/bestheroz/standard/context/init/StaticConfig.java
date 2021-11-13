package com.github.bestheroz.standard.context.init;

import com.github.bestheroz.standard.common.util.AccessBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class StaticConfig {
  public static Boolean LOCAL_ACTIVE_PROFILE_FLAG = null;

  @Autowired
  public void setConstant() {
    if (LOCAL_ACTIVE_PROFILE_FLAG == null) {
      LOCAL_ACTIVE_PROFILE_FLAG =
          AccessBeanUtils.getBean(Environment.class).getActiveProfiles()[0].equals("local");
    }

    log.info(
        """
        {}
        This Demo is managed by bestheroz.
        If you have any questions, send me feedback.
        E-mail: bestheroz@gmail.com
        github: https://github.com/bestheroz
        {}
        """,
        StringUtils.repeat("=", 80),
        StringUtils.repeat("=", 80));
  }
}
