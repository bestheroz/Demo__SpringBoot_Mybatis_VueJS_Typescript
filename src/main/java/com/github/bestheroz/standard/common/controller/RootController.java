package com.github.bestheroz.standard.common.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController implements ErrorController {
    final static private String INDEX_HTML = "index.html";

    @GetMapping(value = {"/", "/login", "/error"})
    public String root() {
        return INDEX_HTML;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

