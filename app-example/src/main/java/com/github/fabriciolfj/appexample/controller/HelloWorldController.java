package com.github.fabriciolfj.appexample.controller;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloWorldController {

    @GetMapping
    public String hello() {
        String salt = KeyGenerators.string().generateKey();
        String password = "secret";
        String valueToEncrypt = "HELLO";
        TextEncryptor e = Encryptors.queryableText(password, salt);
        String encrypted1 = e.encrypt(valueToEncrypt);

        return encrypted1;
    }
}
