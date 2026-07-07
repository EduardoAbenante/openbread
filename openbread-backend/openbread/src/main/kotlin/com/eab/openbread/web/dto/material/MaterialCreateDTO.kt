package com.eab.openbread.web.dto.material

import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.model.RawMaterial
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
@Schema(description = "Material creation DTO")
data class MaterialCreateDTO (
    @field:Schema(
        description = "New material name",
        example = "Bread",
        required = true
    )
    @field:Size(min = 6, max = 100, message = "{val.material.name.size}")
    val name: String,

    @field:Schema(
        description = "New material description",
        example = "Plain white bread",
        required = true
    )
    @field:Size(max = 100, message = "{val.material.description.size}")
    val description: String?,

    @field:Schema(
        description = "New material category",
        required = true
    )
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