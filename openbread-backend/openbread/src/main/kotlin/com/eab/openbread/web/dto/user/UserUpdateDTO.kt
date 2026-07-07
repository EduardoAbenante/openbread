package com.eab.openbread.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

@Schema(description = "User update DTO.")
data class UserUpdateDTO(

    @field:Schema(
        description = "Updated user name. Optional.",
        example = "John"
    )
    @field:NotBlank(message = "{val.user.name.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.name.numeric}")
    val name: String?,

    @field:Schema(
        description = "Updated user surname. Optional.",
        example = "Doe"
    )
    @field:NotBlank(message = "{val.user.surname.empty}")
    @field:Pattern(regexp = "^[^0-9]*$", message = "{val.user.surname.numeric}")
    val surname: String?,

    @field:Schema(
        description = "Updated phone number. Optional.",
        example = "612345678"
    )
    @field:Pattern(regexp = "^\\d{7,15}$|^$", message = "{val.user.phone.format}")
    val phone: String?,

    @field:Schema(
        description = "Updated postal code. Optional.",
        example = "28013"
    )
    @field:Pattern(regexp = "^[a-zA-Z0-9]{3,10}$|^$", message = "{val.user.postalCode.format}")
    val postalCode: String?,

    @field:Schema(
        description = "Indicates whether the user is active. Optional.",
        example = "true"
    )
    val active: Boolean?
)
