package com.eab.openbread.domain.service

import com.eab.openbread.domain.repository.UserRepository
import com.eab.openbread.web.dto.login.LoginRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw RuntimeException("User not found")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw RuntimeException("Incorrect password")
        }

        return jwtService.generateToken(user.email)
    }
}