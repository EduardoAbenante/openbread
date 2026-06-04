package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import jakarta.validation.constraints.NotNull

data class UserRoleUpdateDTO (
    @field:NotNull(message = "{val.role.empty}")
    val role: Role
)