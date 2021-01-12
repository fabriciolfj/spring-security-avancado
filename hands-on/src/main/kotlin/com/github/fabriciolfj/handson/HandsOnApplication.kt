package com.github.fabriciolfj.handson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HandsOnApplication

fun main(args: Array<String>) {
	runApplication<HandsOnApplication>(*args)
}
