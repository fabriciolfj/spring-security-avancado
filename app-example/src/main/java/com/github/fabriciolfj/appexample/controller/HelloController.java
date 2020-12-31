package com.github.fabriciolfj.appexample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/home")
    public String home() {
        return "home.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }

    @GetMapping("/ciao")
    public String ciao() {
        return "ciao teste";
    }

    @GetMapping("/hola")
    public String hola() {
        return "hola teste";
    }
}
