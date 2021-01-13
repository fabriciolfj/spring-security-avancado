package com.github.fabriciolfj.business.domain.core.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class InitialAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var manager: AuthenticationManager

    @Value("\${jwt.signing.key}")
    private lateinit var signingKey: String

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        var username = request.getHeader("username")
        var password = request.getHeader("password")
        var code = request.getHeader("code")

        if (code == null) {
            var a = UsernamePasswordAuthentication(username, password)
            manager.authenticate(a)
            return
        }

        var a = OtpAuthentication(username, code)
        manager.authenticate(a)
        createJwt(response, username)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath != "/login"
    }

    fun createJwt(response: HttpServletResponse, username: String) {
        var key = Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))
        var jwt = Jwts.builder()
            .setClaims(mapOf("username" to username))
            .signWith(key)
            .compact()

        response.setHeader("Authorization", jwt)
    }


}