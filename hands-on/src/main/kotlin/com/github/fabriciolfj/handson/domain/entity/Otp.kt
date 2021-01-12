package com.github.fabriciolfj.handson.domain.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Otp (@Id var username: String, var code: String) {

    constructor() : this("", "")
}