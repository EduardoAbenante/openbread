package com.eab.openbread.domain.service

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.repository.UserRepository
import com.eab.openbread.web.dto.login.LoginRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val jwtService = mockk<JwtService>()

    private val authService = AuthService(userRepository, passwordEncoder, jwtService)

    private val dummyUser = User(
        nif = "12345678A",
        name = "Edu",
        surname = "Alvarez",
        email = "edu@openbread.com",
        password = "hashed_password",
        role = Role.ADMIN
    )

    @Test
    fun `login exitoso deberia retornar un token JWT valido`() {
        val request = LoginRequest(email = "edu@openbread.com", password = "raw_password")

        every { userRepository.findByEmail(request.email) } returns dummyUser
        every { passwordEncoder.matches(request.password, dummyUser.password) } returns true
        every { jwtService.generateToken(dummyUser.email) } returns "mocked-jwt-token"

        val token = authService.login(request)

        assertThat(token).isEqualTo("mocked-jwt-token")

        verify(exactly = 1) { userRepository.findByEmail(request.email) }
        verify(exactly = 1) { passwordEncoder.matches(request.password, dummyUser.password) }
        verify(exactly = 1) { jwtService.generateToken(dummyUser.email) }
    }

    @Test
    fun `login deberia lanzar excepcion si el usuario no existe`() {
        val request = LoginRequest(email = "no_existe@openbread.com", password = "any_password")
        every { userRepository.findByEmail(request.email) } returns null

        val exception = assertThrows<RuntimeException> {
            authService.login(request)
        }

        assertThat(exception.message).isEqualTo("User not found")

        verify(exactly = 1) { userRepository.findByEmail(request.email) }
        verify(exactly = 0) { passwordEncoder.matches(any(), any()) }
        verify(exactly = 0) { jwtService.generateToken(any()) }
    }

    @Test
    fun `login deberia lanzar excepcion si la contrasena es incorrecta`() {
        val request = LoginRequest(email = "edu@openbread.com", password = "wrong_password")

        every { userRepository.findByEmail(request.email) } returns dummyUser
        every { passwordEncoder.matches(request.password, dummyUser.password) } returns false

        val exception = assertThrows<RuntimeException> {
            authService.login(request)
        }

        assertThat(exception.message).isEqualTo("Incorrect password")

        verify(exactly = 1) { userRepository.findByEmail(request.email) }
        verify(exactly = 1) { passwordEncoder.matches(request.password, dummyUser.password) }
        verify(exactly = 0) { jwtService.generateToken(any()) }
    }
}