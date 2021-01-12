package com.github.fabriciolfj.handson.domain.repository

import com.github.fabriciolfj.handson.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, String> {

    fun findByUsername(username: String) : Optional<User>
}