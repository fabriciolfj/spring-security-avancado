package com.github.fabriciolfj.handson.api

import com.github.fabriciolfj.handson.domain.entity.Otp
import com.github.fabriciolfj.handson.domain.entity.User
import com.github.fabriciolfj.handson.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class AuthController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/user/add")
    fun addUser(@RequestBody user: User) {
        userService.addUser(user)
    }

    @PostMapping("/user/auth")
    fun auth(@RequestBody user: User) {
        userService.auth(user)
    }

    @PostMapping("/otp/check")
    fun check(@RequestBody otp: Otp, response: HttpServletResponse) {
        if (userService.check(otp)) {
            response.status = HttpServletResponse.SC_OK
            return
        }

        response.status = HttpServletResponse.SC_FORBIDDEN
    }
}