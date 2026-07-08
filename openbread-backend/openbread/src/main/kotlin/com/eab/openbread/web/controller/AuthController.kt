package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.AuthService
import com.eab.openbread.web.dto.login.LoginRequest
import com.eab.openbread.web.dto.login.LoginResponse
import com.eab.openbread.web.openapi.ApiValidationError
import com.eab.openbread.web.openapi.ApiUnauthorizedError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
class AuthController(
    private val authService: AuthService
) {

    @Operation(
        summary = "User login",
        description = "Authenticates a user with email and password and returns an access token."
    )
    @ApiResponse(responseCode = "200", description = "Login successful (token returned)")
    @ApiValidationError
    @ApiUnauthorizedError
    @SecurityRequirements
    @PostMapping("/login")
    fun login(
        @SwaggerRequestBody(description = "Login credentials.", required = true)
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        val token = authService.login(request)
        return ResponseEntity.ok(LoginResponse(token))
    }
}