package com.github.bestheroz.standard.context.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class InitWebConstantConfiguration {
  @Autowired
  public void setConstant() {
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
