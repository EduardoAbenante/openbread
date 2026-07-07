package com.eab.openbread.web.dto.login

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Login request DTO")
data class LoginRequest(

    @field:Schema(
        description = "User mail.",
        example = "usuario@ejemplo.com",
        required = true
    )
    @field:NotBlank(message = "{val.user.email.empty}")
    val email: String,

    @field:Schema(
        description = "User password.",
        example = "••••••••",
        required = true
    )
    @field:NotBlank(message = "{val.user.password.empty}")
    val password: String
)
