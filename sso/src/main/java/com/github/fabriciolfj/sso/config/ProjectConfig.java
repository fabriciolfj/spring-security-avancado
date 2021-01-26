package com.github.fabriciolfj.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    /*@Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var c =  clientRegistration();
        return new InMemoryClientRegistrationRepository(c);
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.oauth2Client(c -> {
            c.clientRegistrationRepository(clientRegistrationRepository());
        });*/
        http.oauth2Login();
        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    /*private ClientRegistration clientRegistration() {
        return CommonOAuth2Provider.GITHUB
                .getBuilder("github")
                .clientId("6a1a88ffdd8056d30966")
                .clientSecret("298c1d3b309d875c3569b91847d513f981e11b11")
                .build();
    }*/
}
