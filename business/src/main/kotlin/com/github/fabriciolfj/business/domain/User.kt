package com.github.fabriciolfj.business.domain

data class User(var username: String, var password: String, var code: String?) {

    constructor(username: String, password: String) : this(username, password, null)
    constructor() : this("", "", null)
}