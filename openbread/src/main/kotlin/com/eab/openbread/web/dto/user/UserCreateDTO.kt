package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserCreateDTO (
    @field:NotBlank(message = "User NIF can't be empty")
    val nif: String,

    @field:NotBlank(message = "User name can't be empty")
    val name: String,

    @field:NotBlank(message = "User surname can't be empty")
    val surname: String,

    @field:NotBlank(message = "User email can't be empty")
    val email:String,

    @field:NotBlank(message = "User password can't be empty")
    @field:Size(min = 6, max = 100, message = "User password can't be empty")
    val password: String,

    val phone: String?,
    val postalCode: String?,
    val role: Role

)

fun UserCreateDTO.toEntity(hashedPassword: String): User {
    return User(
        id = 0,
        NIF = this.nif,
        name = this.name,
        surname = this.surname,
        email = this.email,
        password = hashedPassword,
        phone = this.phone,
        postalCode = this.postalCode,
        photoUrl = null,
        role = this.role,
        active = true
    )
}

