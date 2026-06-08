package com.eab.openbread.web.dto.user

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User


data class UserResponseDTO (
    val id: Long,
    val nif: String,
    val name: String,
    val surname: String,
    val email:String,
    val phone: String?,
    val postalCode: String?,
    val role: Role,
    val active: Boolean,
    val photoUrl: String?
)

fun toUserResponseDTO(user: User): UserResponseDTO {
    var newDTO = UserResponseDTO(
        id = user.id,
        nif = user.nif,
        name = user.name,
        surname = user.surname,
        email = user.email,
        phone = user.phone,
        postalCode = user.postalCode,
        role = user.role,
        active = user.active,
        photoUrl = user.photoUrl
    )
    return newDTO
}