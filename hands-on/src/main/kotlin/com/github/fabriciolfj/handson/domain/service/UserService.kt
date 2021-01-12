package com.github.fabriciolfj.handson.domain.service

import com.github.fabriciolfj.handson.domain.entity.Otp
import com.github.fabriciolfj.handson.domain.entity.User
import com.github.fabriciolfj.handson.domain.repository.OtpRepository
import com.github.fabriciolfj.handson.domain.repository.UserRepository
import com.github.fabriciolfj.handson.infra.generateCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(propagation = Propagation.REQUIRED)
class UserService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var otpRepository: OtpRepository

    fun addUser(user: User)  {
        user.password = passwordEncoder.encode(user.password)
        userRepository.save(user)
    }

    fun auth(user: User) {
        userRepository.findByUsername(user.username!!)
            .filter {
                passwordEncoder.matches(user.password, it.password)
            }
            .map { renewOtp(user) }
            .orElseThrow { throw BadCredentialsException("Bad credentials") }
    }

    fun check(otp: Otp)  = otpRepository.findByUsername(otp.username)
            .map { it.code == otp.code }
            .orElse(false)

    private fun renewOtp(u: User) {
        var code = generateCode()
        otpRepository.findByUsername(u.username!!)
            .map { it.code = code }
            .orElseGet {
                var otp = Otp()
                otp.code = code
                otp.username = u.username!!
                otpRepository.save(otp)
            }
    }
}