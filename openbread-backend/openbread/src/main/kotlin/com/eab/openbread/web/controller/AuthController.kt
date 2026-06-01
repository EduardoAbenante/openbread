package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.AuthService
import com.eab.openbread.web.dto.login.LoginRequest
import com.eab.openbread.web.dto.login.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val token = authService.login(request)
        return ResponseEntity.ok(LoginResponse(token))
    }
}