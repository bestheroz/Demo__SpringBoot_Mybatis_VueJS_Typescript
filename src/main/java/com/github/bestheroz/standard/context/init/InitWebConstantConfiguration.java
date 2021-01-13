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
        "\n{}\nThis Demo is managed by bestheroz.\nIf you have any questions, send me feedback.\nE-mail: bestheroz@gmail.com\ngithub: https://github.com/bestheroz\n{}",
        StringUtils.repeat("=", 80),
        StringUtils.repeat("=", 80));
  }
}
