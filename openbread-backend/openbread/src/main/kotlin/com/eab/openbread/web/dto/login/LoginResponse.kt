package com.eab.openbread.web.dto.login

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Login Token")
class LoginResponse (
    @field:Schema(description = "JWT token")
    val token: String
)