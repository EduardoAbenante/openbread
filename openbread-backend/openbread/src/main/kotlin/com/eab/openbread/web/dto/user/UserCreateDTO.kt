package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import kotlin.jvm.optionals.*

@Schema(description = "User create DTO.")
data class UserCreateDTO(

    @field:Schema(
        description = "User NIF (tax identifier).",
        example = "12345678A",
        required = true
    )
    @field:NotBlank(message = "{val.user.nif.empty}")
    val nif: String,

    @field:Schema(
        description = "User name (no numbers).",
        example = "John",
        required = true
    )
    @field:NotBlank(message = "{val.user.name.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.name.numeric}")
    val name: String,

    @field:Schema(
        description = "User surname (no numbers).",
        example = "Doe",
        required = true
    )
    @field:NotBlank(message = "{val.user.surname.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.surname.numeric}")
    val surname: String,

    @field:Schema(
        description = "User email.",
        example = "john.doe@example.com",
        required = true
    )
    @field:NotBlank(message = "{val.user.email.empty}")
    @field:Email(message = "{val.user.email.format}")
    val email: String,

    @field:Schema(
        description = "User password (min length and strength requirements).",
        example = "MyP@ssw0rd",
        required = true
    )
    @field:NotBlank(message = "{val.user.password.empty}")
    @field:Size(min = 6, max = 100, message = "{val.user.password.size}")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\\$!%*#?&])[A-Za-z\\d@\\$!%*#?&]{6,}$",
        message = "{val.user.password.strength}"
    )
    val password: String,

    @field:Schema(
        description = "User phone number. Optional.",
        example = "612345678",
        required = false
    )
    @field:Pattern(regexp = "^\\d{7,15}$|^$", message = "{val.user.phone.format}")
    val phone: String?,

    @field:Schema(
        description = "User postal code. Optional.",
        example = "28013",
        required = false
    )
    @field:Pattern(regexp = "^[a-zA-Z0-9]{3,10}$|^$", message = "{val.user.postalCode.format}")
    val postalCode: String?,

    @field:Schema(
        description = "User role.",
        example = "USER",
        required = true
    )
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
