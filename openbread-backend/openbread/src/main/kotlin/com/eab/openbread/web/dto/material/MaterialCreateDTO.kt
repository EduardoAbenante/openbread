package com.eab.openbread.web.dto.material

import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.model.RawMaterial
import jakarta.validation.constraints.Size

data class MaterialCreateDTO (
    @field:Size(min = 6, max = 100, message = "{val.material.name.size}")
    val name: String,

    @field:Size(max = 100, message = "{val.material.description.size}")
    val description: String?,

    val categoryId: Long?

)

fun MaterialCreateDTO.toEntity(): RawMaterial {
    return RawMaterial(
        id = 0,
        name = this.name,
        description = this.description,
        active = true
    )
}