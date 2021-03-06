package com.github.fabriciolfj.appexample.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAsync
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CustomCsrfTokenRepository();
    }

    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Autowired
    private AuthenticationProvider authenticationProvider;*/

    /*@Autowired
    private StaticKeyAuthenticationFilter filter;*/

   /* @Bean
    public UserDetailsService userDetailsService(final DataSource dataSource) {
        //return new JdbcUserDetailsManager(dataSource);
        var manager =  new InMemoryUserDetailsManager();
        var user1 = User.withUsername("fabricio")
                                        .password("12345")
                                        .authorities("READ")
                                        //.roles("ADMIN")
                                        .build();

        var user2 = User.withUsername("jane")
                                    .password("12345")
                                    .authorities("READ")
                                    //.roles("MANAGER")
                                    .build();

        manager.createUser(user1);
        manager.createUser(user2);
        return manager;
    }*/

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        /*Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder("bcrypt", encoders);
        return NoOpPasswordEncoder.getInstance();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource source = httpServletRequest -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("exemplo.com", "http://localhost:8080"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT","DELETE"));
                return config;
            };

            c.configurationSource(source);
        });

        http.csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();
        /*http.csrf(c -> {
            c.ignoringAntMatchers("/ciao");
            c.csrfTokenRepository(csrfTokenRepository());
        });

        http.authorizeRequests().anyRequest().permitAll();*/

        /*http.authorizeRequests()
                .anyRequest().authenticated();

        http.formLogin()
                .defaultSuccessUrl("/main", true);*/

        /*http.addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class)
                .authorizeRequests().anyRequest().permitAll();*/

        /*http.addFilterAt(filter, BasicAuthenticationFilter.class) //colocar o filter na mesma posição
                .authorizeRequests()
                .anyRequest().permitAll();*/

        /*http.addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().permitAll();*/

        /*http.httpBasic(c -> {
            c.realmName("OTHER");
            c.authenticationEntryPoint(new CustomEntryPoint());
        });

        http.formLogin()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .httpBasic(); // add o filtro BasicAuthenticationFilter

        http.authorizeRequests()
                .anyRequest()
                .authenticated();*/

        //String expression = "hasAuthority('read') and !hasAuthority('delete')";

        //http.httpBasic();
        //http.csrf().disable();
        //http.authorizeRequests()
          //      .regexMatchers(".*/(us|br)+/(en|br).*")
            //    .authenticated()
              //  .anyRequest() //para todos os outros endpoints
                //.hasAuthority("premium"); //precisa de autorizacao premium
                //.mvcMatchers("/hello")
                //.authenticated();
                //.mvcMatchers("/product/{code:^[0-9]*$}")// nesse path, quero que receba apenas digitos, caso aparece letras negue
                //.permitAll()
                //.anyRequest().denyAll();
                //.mvcMatchers(HttpMethod.GET, "/a").authenticated()
                //.mvcMatchers(HttpMethod.POST, "/a").permitAll()
                //.mvcMatchers("/a/b/**")
                //.authenticated()
                //.anyRequest()
                //.permitAll();
                //.denyAll();
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }*/

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var userDetailsService = new InMemoryUserDetailsManager();
        var user = User.withUsername("fabricio")
                .password("123")
                .authorities("read")
                .build();
        userDetailsService.createUser(user);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
        auth.authenticationProvider(customAuthenticationProvider);
    }

    Podemos configurar dentro do authenticationManagerBuilder
    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();
        var user = User.withUsername("fabricio")
                .password("123")
                .authorities("read")
                .build();
        userDetailsService.createUser(user);
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }*/
}
