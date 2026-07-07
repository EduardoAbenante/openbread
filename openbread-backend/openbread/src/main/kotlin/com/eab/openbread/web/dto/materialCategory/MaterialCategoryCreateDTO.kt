package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.CategoryColor
import com.eab.openbread.domain.model.MaterialCategory
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Material category create DTO.")
class MaterialCategoryCreateDTO(

    @field:Schema(
        description = "Category name",
        example = "Bakery",
        required = true
    )
    @field:Size(min = 6, max = 100, message = "{val.materialCategory.name.size}")
    var name: String,

    @field:Schema(
        description = "Category description",
        example = "Categories related to bread and bakery products"
    )
    @field:Size(max = 100, message = "{val.materialCategory.description.size}")
    var description: String?,

    @field:Schema(
        description = "Category color (string value of CategoryColor enum). If null/blank/invalid, defaults to GRAY.",
        example = "GRAY"
    )
    var color: String? = null
)

fun MaterialCategoryCreateDTO.toEntity(): MaterialCategory {
    val entityColor = try {
        if (!this.color.isNullOrBlank()) CategoryColor.valueOf(this.color!!.uppercase())
        else CategoryColor.GRAY
    } catch (e: IllegalArgumentException) {
        CategoryColor.GRAY
    }

    return MaterialCategory(
        id = 0,
        name = this.name,
        description = this.description,
        color = entityColor,
        active = true
    )
}
