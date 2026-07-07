package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.CategoryColor
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Material category update DTO.")
class MaterialCategoryUpdateDTO(

    @field:Schema(
        description = "Updated category name. Optional.",
        example = "Bakery"
    )
    val name: String?,

    @field:Schema(
        description = "Updated category description. Optional.",
        example = "Categories related to bread and bakery products"
    )
    val description: String?,

    @field:Schema(
        description = "Updated category color (CategoryColor enum). Optional.",
        example = "GRAY"
    )
    val color: CategoryColor?
)
