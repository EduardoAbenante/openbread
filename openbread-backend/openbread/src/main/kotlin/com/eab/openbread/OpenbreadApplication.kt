package com.eab.openbread

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class OpenbreadApplication

fun main(args: Array<String>) {
	runApplication<OpenbreadApplication>(*args)
}
