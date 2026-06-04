package com.eab.openbread.web.dto.user

import jakarta.validation.constraints.*

data class UserUpdateDTO(

    @field:NotBlank(message = "{val.user.name.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.name.numeric}")
    val name: String?,

    @field:NotBlank(message = "{val.user.surname.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.surname.numeric}")
    val surname: String?,

    @field:Pattern(regexp = "^\\d{7,15}$|^$", message = "{val.user.phone.format}")
    val phone: String?,

    @field:Pattern(regexp = "^[a-zA-Z0-9]{3,10}$|^$", message = "{val.user.postalCode.format}")
    val postalCode: String?,

    val active: Boolean?
)
