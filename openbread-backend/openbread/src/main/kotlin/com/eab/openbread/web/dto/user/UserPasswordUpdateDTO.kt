package com.eab.openbread.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "User password update DTO.")
data class UserPasswordUpdateDTO(

    @field:Schema(
        description = "New password. Must meet length and strength requirements.",
        example = "MyP@ssw0rd",
        required = true
    )
    @field:NotBlank(message = "{val.user.password.empty}")
    @field:Size(min = 6, max = 100, message = "{val.user.password.size}")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\\$!%*#?&])[A-Za-z\\d@\\$!%*#?&]{6,}\\$",
        message = "{val.user.password.strength}"
    )
    val password: String
)
