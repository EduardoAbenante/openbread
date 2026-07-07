package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "User role update DTO.")
data class UserRoleUpdateDTO(

    @field:Schema(
        description = "New user role.",
        example = "USER",
        required = true
    )
    @field:NotNull(message = "{val.role.empty}")
    val role: Role
)
