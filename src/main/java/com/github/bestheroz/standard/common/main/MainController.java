package com.github.bestheroz.standard.common.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = {"/"})
    public String indexPage() {
        return "index";
    }
}
