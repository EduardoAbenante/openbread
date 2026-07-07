package com.eab.openbread

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class OpenbreadApplication

fun main(args: Array<String>) {
	SpringApplication.run(OpenbreadApplication::class.java, *args)
}

