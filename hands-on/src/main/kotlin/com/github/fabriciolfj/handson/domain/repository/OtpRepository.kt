package com.github.fabriciolfj.handson.domain.repository

import com.github.fabriciolfj.handson.domain.entity.Otp
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OtpRepository : JpaRepository<Otp, String> {

    fun findByUsername(username: String) : Optional<Otp>
}