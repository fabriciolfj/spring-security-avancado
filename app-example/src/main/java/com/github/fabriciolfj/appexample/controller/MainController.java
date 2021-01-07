package com.github.fabriciolfj.appexample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main() {
        return "main.html";
    }

    @PostMapping("/test")
    @ResponseBody
    //@CrossOrigin("http://localhost:8080")
    public String test() {
        return "HELLO";
    }
}
