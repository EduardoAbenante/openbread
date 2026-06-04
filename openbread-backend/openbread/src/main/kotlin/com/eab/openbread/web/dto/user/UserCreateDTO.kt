package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import jakarta.validation.constraints.*

data class UserCreateDTO (
    @field:NotBlank(message = "{val.user.nif.empty}")
    val nif: String,

    @field:NotBlank(message = "{val.user.name.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.name.numeric}")
    val name: String,

    @field:NotBlank(message = "{val.user.surname.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.surname.numeric}")
    val surname: String,

    @field:NotBlank(message = "{val.user.email.empty}")
    @field:Email(message = "{val.user.email.format}")
    val email:String,

    @field:NotBlank(message = "{val.user.password.empty}")
    @field:Size(min = 6, max = 100, message = "{val.user.password.size}")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{6,}\$",
        message = "{val.user.password.strength}"
    )
    val password: String,

    @field:Pattern(regexp = "^\\d{7,15}$|^$", message = "{val.user.phone.format}")
    val phone: String?,

    @field:Pattern(regexp = "^[a-zA-Z0-9]{3,10}$|^$", message = "{val.user.postalCode.format}")
    val postalCode: String?,

    @field:NotNull(message = "{val.role.empty}")
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

