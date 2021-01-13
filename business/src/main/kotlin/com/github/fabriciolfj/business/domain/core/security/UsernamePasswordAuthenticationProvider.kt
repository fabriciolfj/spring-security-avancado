package com.github.fabriciolfj.business.domain.core.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UsernamePasswordAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private lateinit var proxy : AuthenticationServerProxy

    override fun authenticate(authenticate: Authentication?): Authentication {
        var username = authenticate!!.name
        var password = authenticate!!.credentials as String
        proxy.sendAuth(username, password)

        return UsernamePasswordAuthenticationToken(username, password)
    }

    override fun supports(aClass: Class<*>?): Boolean {
        return UsernamePasswordAuthentication::class.java.isAssignableFrom(aClass)
    }
}