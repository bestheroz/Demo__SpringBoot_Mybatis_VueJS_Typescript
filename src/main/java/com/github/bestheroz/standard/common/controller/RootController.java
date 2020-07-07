package com.github.bestheroz.standard.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class RootController implements ErrorController {
    final static private String INDEX_HTML = "index.html";

    @GetMapping(value = {"/", "/index.html"})
    public String root() {
        log.debug("root");
        return INDEX_HTML;
    }

    @GetMapping("/error")
    public String redirectRoot() {
        log.debug("redirectRoot");
        return INDEX_HTML;
    }

    @Override
    public String getErrorPath() {
        log.debug("getErrorPath");
        return INDEX_HTML;
    }
}
