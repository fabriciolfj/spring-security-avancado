package com.github.fabriciolfj.appexample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/home")
    public String home() {
        return "home.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }
}
