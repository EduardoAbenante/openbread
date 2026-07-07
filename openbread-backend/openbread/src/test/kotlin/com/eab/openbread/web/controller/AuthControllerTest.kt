package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.AuthService
import com.eab.openbread.domain.service.JwtService
import com.eab.openbread.web.dto.login.LoginRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.jacksonObjectMapper

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @MockkBean
    private lateinit var authService: AuthService

    @MockkBean
    private lateinit var jwtService: JwtService

    @Test
    fun `login deberia retornar 200 OK y el token en formato JSON cuando las credenciales son validas`() {
        val loginRequest = LoginRequest(email = "panadero@openbread.com", password = "SecretPassword123")
        val fakeToken = "jwt.token.falso.simulado"

        every { authService.login(loginRequest) } returns fakeToken

        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value(fakeToken) }
        }

        verify(exactly = 1) { authService.login(loginRequest) }
    }

    @Test
    fun `login deberia retornar 400 Bad Request si el cuerpo de la peticion JSON no cumple las validaciones`() {
        val invalidRequest = LoginRequest(email = "", password = "123")

        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { authService.login(any()) }
    }
}