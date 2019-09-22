package com.github.bestheroz.standard.common.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping(value = "/swagger")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
