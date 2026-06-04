package com.eab.openbread.web.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserPasswordUpdateDTO (
    @field:NotBlank(message = "{val.user.password.empty}")
    @field:Size(min = 6, max = 100, message = "{val.user.password.size}")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{6,}\$",
        message = "{val.user.password.strength}"
    )
    val password: String
)

