package com.github.bestheroz.standard.common.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController implements ErrorController {
  @RequestMapping(value = {"/error"})
  public String root() {
    return "index.html";
  }
}
