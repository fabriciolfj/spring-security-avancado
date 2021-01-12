package com.github.fabriciolfj.handson.infra

import java.lang.RuntimeException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

fun generateCode() : String {
    var code = "";

    try {
        var random = SecureRandom.getInstanceStrong()
        var c = random.nextInt(9000) + 1000
        code = c.toString()
    } catch (e : NoSuchAlgorithmException) {
        throw RuntimeException("Problema when generating the random code")
    }

    return code
}