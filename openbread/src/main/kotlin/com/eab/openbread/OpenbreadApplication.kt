package com.eab.openbread

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OpenbreadApplication

fun main(args: Array<String>) {
	runApplication<OpenbreadApplication>(*args)
}
