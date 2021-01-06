package com.github.fabriciolfj.appexample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var requestId = httpRequest.getHeader("Request-Id");

        logger.info("Successfully authenticated request id {}", requestId);

        filterChain.doFilter(httpRequest, httpServletResponse);
    }
}
