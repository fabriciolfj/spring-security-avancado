package com.github.fabriciolfj.business

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BusinessApplication

fun main(args: Array<String>) {
	runApplication<BusinessApplication>(*args)
}
