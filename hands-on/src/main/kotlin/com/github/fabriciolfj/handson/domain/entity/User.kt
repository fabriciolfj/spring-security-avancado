package com.github.fabriciolfj.handson.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(@Id @JsonProperty("username") var username: String?, @JsonProperty("password") var password: String?) {

    constructor() : this ("", "")
}