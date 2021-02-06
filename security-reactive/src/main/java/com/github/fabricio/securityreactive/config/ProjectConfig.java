package com.github.fabricio.securityreactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class ProjectConfig {
    
    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        var u = User.withUsername("fabricio")
                .password("12345")
                .authorities("read")
                .roles("ADMIN")
                .build();

        var u2 = User.withUsername("lucas")
                .password("12345")
                .authorities("read")
                .roles("USER")
                .build();

        var uds = new MapReactiveUserDetailsService(u, u2);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/hello").authenticated()
                .anyExchange().permitAll()
                .and()
                .httpBasic()
                .and().build();
    }
}
