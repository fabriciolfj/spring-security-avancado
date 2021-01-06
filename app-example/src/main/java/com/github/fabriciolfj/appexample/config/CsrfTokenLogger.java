package com.github.fabriciolfj.appexample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class CsrfTokenLogger implements Filter {

    private Logger logger = LoggerFactory.getLogger(CsrfTokenLogger.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var o = servletRequest.getAttribute("_csrf");
        var token = (CsrfToken) o;
        logger.info("CSRF token {}", token.getToken());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
