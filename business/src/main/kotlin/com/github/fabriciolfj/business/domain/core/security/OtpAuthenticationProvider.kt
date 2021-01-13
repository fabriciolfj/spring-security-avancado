package com.github.fabriciolfj.business.domain.core.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class OtpAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private lateinit var proxy: AuthenticationServerProxy

    override fun authenticate(authenticate: Authentication?): Authentication {
        var username = authenticate!!.name
        var code = authenticate.credentials as String
        var result = proxy.sendOtp(username, code)

        if (result) {
            return OtpAuthentication(username, code)
        }

        throw BadCredentialsException("Bad credentials")
    }

    override fun supports(p0: Class<*>?): Boolean {
        return OtpAuthentication::class.java.isAssignableFrom(p0);
    }
}