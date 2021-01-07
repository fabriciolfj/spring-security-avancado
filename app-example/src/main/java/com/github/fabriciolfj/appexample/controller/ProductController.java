package com.github.fabriciolfj.appexample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
@RequestMapping("/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/add")
    public String add(@RequestParam String name)  {
        logger.info("Adding product: {}", name);
        return "main.html";
    }


    /*@GetMapping("/product/{code}")
    public String productCode(@PathVariable("code") final String code) {
        return code;
    }*/


}
