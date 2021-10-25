package com.github.bestheroz.standard.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RootController {
  private static final String INDEX_HTML = "index.html";

  @RequestMapping(
      value = {"/", "/login", "/error"},
      method = {
        RequestMethod.OPTIONS,
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.PATCH,
        RequestMethod.DELETE
      })
  public String root() {
    return INDEX_HTML;
  }
}
