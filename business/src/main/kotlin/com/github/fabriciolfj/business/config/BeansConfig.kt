package com.github.fabriciolfj.business.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class BeansConfig {

    @Bean
    fun restTemplate() = RestTemplate()
}