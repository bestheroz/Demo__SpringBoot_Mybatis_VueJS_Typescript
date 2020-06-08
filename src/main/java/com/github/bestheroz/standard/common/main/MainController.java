package com.github.bestheroz.standard.common.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/login", "/admin/**", "/board/**", "/Code*"})
    public String entry() {
        return "index";
    }

}