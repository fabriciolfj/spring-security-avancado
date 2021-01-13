package com.github.fabriciolfj.business.domain.core.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UsernamePasswordAuthentication : UsernamePasswordAuthenticationToken {
    constructor(principal: Any?, credentials: Any?, authorities: Collection<GrantedAuthority?>?) : super(
        principal,
        credentials,
        authorities
    ) {
    }

    constructor(principal: Any?, credentials: Any?) : super(principal, credentials) {}


}