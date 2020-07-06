package com.github.bestheroz.standard.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PreDestroy;

@Controller
@Slf4j
public class RootController implements ErrorController {
    final static private String INDEX_HTML = "index.html";

    @GetMapping("/")
    public String root() {
        return INDEX_HTML;
    }

    @GetMapping("/error")
    public String redirectRoot() {
        return INDEX_HTML;
    }

    @Override
    @PreDestroy
    public String getErrorPath() {
        return INDEX_HTML;
    }
}
