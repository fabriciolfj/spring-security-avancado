package com.github.fabricio.securityreactive.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @GetMapping("/ciao")
    public Mono<String> ciao() {
        return Mono.just("ciao");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hello")
    public Mono<String> get() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(a -> Mono.just("hello " + a.getName()));
    }
}
