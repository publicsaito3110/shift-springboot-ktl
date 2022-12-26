package com.shift

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.Clock

@SpringBootApplication
class ShiftSpringbootKtlApplication

fun main(args: Array<String>) {
	runApplication<ShiftSpringbootKtlApplication>(*args)
}
