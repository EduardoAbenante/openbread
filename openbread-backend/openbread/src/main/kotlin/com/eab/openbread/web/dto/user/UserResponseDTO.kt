package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User response DTO.")
data class UserResponseDTO(

    @field:Schema(
        description = "User identifier.",
        example = "1",
        required = true
    )
    val id: Long,

    @field:Schema(
        description = "User NIF (tax identifier).",
        example = "12345678A",
        required = true
    )
    val nif: String,

    @field:Schema(
        description = "User name.",
        example = "John",
        required = true
    )
    val name: String,

    @field:Schema(
        description = "User surname.",
        example = "Doe",
        required = true
    )
    val surname: String,

    @field:Schema(
        description = "User email.",
        example = "john.doe@example.com",
        required = true
    )
    val email: String,

    @field:Schema(
        description = "User phone number.",
        example = "612345678"
    )
    val phone: String?,

    @field:Schema(
        description = "User postal code.",
        example = "28013"
    )
    val postalCode: String?,

    @field:Schema(
        description = "User role.",
        example = "USER",
        required = true
    )
    val role: Role,

    @field:Schema(
        description = "Indicates whether the user is active.",
        example = "true",
        required = true
    )
    val active: Boolean,

    @field:Schema(
        description = "URL of the user photo/avatar.",
        example = "https://example.com/avatars/user123.png"
    )
    val photoUrl: String?,

    @field:Schema(
        description = "User creation timestamp (as a string).",
        example = "2026-07-07T10:15:30Z"
    )
    val createdAt: String? = null,

    @field:Schema(
        description = "User last update timestamp (as a string).",
        example = "2026-07-10T08:22:10Z"
    )
    val updatedAt: String? = null
)

fun toUserResponseDTO(user: User): UserResponseDTO {
    return UserResponseDTO(
        id = user.id,
        nif = user.nif,
        name = user.name,
        surname = user.surname,
        email = user.email,
        phone = user.phone,
        postalCode = user.postalCode,
        role = user.role,
        active = user.active,
        photoUrl = user.photoUrl,
        createdAt = user.createdAt?.toString(),
        updatedAt = user.updatedAt?.toString(),
    )
}
