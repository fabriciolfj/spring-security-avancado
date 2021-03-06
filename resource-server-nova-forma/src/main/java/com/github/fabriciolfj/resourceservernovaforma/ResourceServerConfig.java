package com.github.fabriciolfj.resourceservernovaforma;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${token.uri}")
    private String uri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(
                        c -> c.opaqueToken(
                                o -> {
                                    o.introspectionUri(uri);
                                    o.introspectionClientCredentials("resourceserver", "resourceserversecret");
                                }
                        )
                );
    }
}
