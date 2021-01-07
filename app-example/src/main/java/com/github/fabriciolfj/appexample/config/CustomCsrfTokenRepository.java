package com.github.fabriciolfj.appexample.config;

import com.github.fabriciolfj.appexample.entity.Token;
import com.github.fabriciolfj.appexample.repository.JpaTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    @Autowired
    private JpaTokenRepository jpaTokenRepository;

    @Override
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        String uuid = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);
    }

    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        var identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        jpaTokenRepository.findTokenByIdentifier(identifier)
                .map(t -> {
                    t.setToken(csrfToken.getToken());
                    return t;
                }).orElseGet(() -> {
                    var token = new Token();
                    token.setToken(csrfToken.getToken());
                    token.setIdentifier(identifier);
                    return jpaTokenRepository.save(token);
                });
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest httpServletRequest) {
        var identifier = httpServletRequest.getHeader("X-IDENTIFIER");

        return jpaTokenRepository.findTokenByIdentifier(identifier)
                .map(t-> new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", t.getToken()))
                .orElseGet(() -> null);
    }
}
