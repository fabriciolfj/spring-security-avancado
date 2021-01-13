package com.github.fabriciolfj.business.domain.core.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Value("\${jwt.signing.key}")
    private lateinit var signingKey: String

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        var claims = getClaims(request)
        var username = claims["username"] as String
        var a = SimpleGrantedAuthority("user")
        var auth = UsernamePasswordAuthentication(username, null, listOf(a))

        SecurityContextHolder.getContext().authentication = auth
        chain.doFilter(request, response)
    }

    private fun getClaims(request: HttpServletRequest) : Claims {
        try {
            var jwt = request.getHeader("Authorization")
            var key = Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .body
        } catch (e: Exception) {
            throw BadCredentialsException("Bard credentials")
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath == "/login"
    }
}