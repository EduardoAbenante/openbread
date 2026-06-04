package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserCreateDTO (
    @field:NotBlank(message = "{val.user.nif.empty}")
    val nif: String,

    @field:NotBlank(message = "{val.user.name.empty}")
    val name: String,

    @field:NotBlank(message = "{val.user.surname.empty}")
    val surname: String,

    @field:NotBlank(message = "{val.user.email.empty}")
    val email:String,

    @field:NotBlank(message = "{val.user.password.empty}")
    @field:Size(min = 6, max = 100, message = "{val.user.password.size}")
    val password: String,

    val phone: String?,
    val postalCode: String?,
    val role: Role

)

fun UserCreateDTO.toEntity(hashedPassword: String): User {
    return User(
        id = 0,
        nif = this.nif,
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

