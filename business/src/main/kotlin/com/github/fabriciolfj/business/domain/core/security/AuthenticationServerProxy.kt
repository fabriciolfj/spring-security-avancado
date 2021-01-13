package com.github.fabriciolfj.business.domain.core.security

import com.github.fabriciolfj.business.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class AuthenticationServerProxy {

    @Autowired
    private lateinit var rest : RestTemplate

    @Value("\${auth.server.base.url}")
    private lateinit var baseUrl: String

    fun sendAuth(username: String, password: String) {
        var url = "$baseUrl/user/auth"
        var body = User(username, password)
        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Accept", "application/json")

        var request = HttpEntity(body, headers)
        rest.postForEntity(url, request, Void::class.java)
    }

    fun sendOtp(username: String, code: String) : Boolean {
        var url = "$baseUrl/otp/check"
        var body = User()
        body.username = username
        body.code = code

        var request = HttpEntity(body)
        var response = rest.postForEntity(url, request, Void::class.java)

        return response.statusCode == HttpStatus.OK
    }
}