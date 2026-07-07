package com.eab.openbread.web.dto.material

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Material update DTO.")
data class MaterialUpdateDTO(

    @field:Schema(
        description = "Updated material name",
        example = "Bread",
        required = true
    )
    @field:Size(min = 6, max = 100, message = "{val.material.name.size}")
    val name: String,

    @field:Schema(
        description = "Updated material description",
        example = "Plain white bread"
    )
    @field:Size(max = 100, message = "{val.material.description.size}")
    val description: String?,

    @field:Schema(
        description = "Updated category ID associated with the material",
        example = "2"
    )
    val categoryId: Long?,
)
