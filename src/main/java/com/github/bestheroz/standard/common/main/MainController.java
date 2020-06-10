package com.github.bestheroz.standard.common.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping(value = {"/"})
    public String indexPage() {
        return "index";
    }

    @GetMapping(value = {"/error"})
    public String errorPage() {
        return "index";
    }
}
