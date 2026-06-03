package com.eab.openbread.web.dto.user


data class UserUpdateDTO(
    val name: String?,
    val surname: String?,
    val phone: String?,
    val postalCode: String?,
    val active: Boolean?
)
