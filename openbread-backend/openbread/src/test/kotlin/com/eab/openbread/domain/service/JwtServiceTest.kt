package com.eab.openbread.domain.service

import io.jsonwebtoken.JwtException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private val testSecret = "mi-clave-secreta-super-segura-de-pruebas-openbread"

    @BeforeEach
    fun setUp() {
        jwtService = JwtService(testSecret)
    }

    @Test
    fun `deberia generar un token valido y posteriormente extraer el email correctamente`() {
        val email = "panadero@openbread.com"
        val token = jwtService.generateToken(email)

        assertThat(token).isNotBlank()

        assertThat(token.split(".")).hasSize(3)

        val extractedEmail = jwtService.validateToken(token)

        assertThat(extractedEmail).isEqualTo(email)
    }

    @Test
    fun `deberia lanzar excepcion si el token ha sido manipulado o es corrupto`() {
        val email = "panadero@openbread.com"
        val tokenValido = jwtService.generateToken(email)

        val tokenCorrupto = "$tokenValido-un-par-de-letras-maliciosas"

        assertThrows<JwtException> {
            jwtService.validateToken(tokenCorrupto)
        }
    }
}