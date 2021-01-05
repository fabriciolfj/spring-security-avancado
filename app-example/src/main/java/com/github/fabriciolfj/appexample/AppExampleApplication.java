package com.github.fabriciolfj.appexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication//(exclude = {UserDetailsServiceAutoConfiguration.class}) caso não precise
public class AppExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppExampleApplication.class, args);
	}

}
