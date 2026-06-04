package com.eab.openbread.web.dto.login

import jakarta.validation.constraints.NotBlank

data class LoginRequest (
    @field:NotBlank(message = "{val.user.email.empty}")
    val email: String,

    @field:NotBlank(message = "{val.user.password.empty}")
    val password: String
)