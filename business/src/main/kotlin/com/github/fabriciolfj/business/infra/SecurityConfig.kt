package com.github.fabriciolfj.business.infra

import com.github.fabriciolfj.business.domain.core.security.InitialAuthenticationFilter
import com.github.fabriciolfj.business.domain.core.security.JwtAuthenticationFilter
import com.github.fabriciolfj.business.domain.core.security.OtpAuthenticationProvider
import com.github.fabriciolfj.business.domain.core.security.UsernamePasswordAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var initialAuthentication: InitialAuthenticationFilter

    @Autowired
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var otpAuthenticationProvider: OtpAuthenticationProvider

    @Autowired
    private lateinit var usernamePasswordAuthenticationProvider: UsernamePasswordAuthenticationProvider

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(otpAuthenticationProvider)
            .authenticationProvider(usernamePasswordAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()

        http!!.addFilterAt(initialAuthentication, BasicAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)

        http.authorizeRequests()
            .anyRequest()
            .authenticated()
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }
}