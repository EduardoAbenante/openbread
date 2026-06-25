package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.CategoryColor
import com.eab.openbread.domain.model.MaterialCategory
import jakarta.validation.constraints.Size

class MaterialCategoryCreateDTO (
    @field:Size(min = 6, max = 100, message = "{val.materialCategory.name.size}")
    var name: String,
    @field:Size(max = 100, message = "{val.materialCategory.description.size}")
    var description: String?,
    var color: String? = null


)

fun MaterialCategoryCreateDTO.toEntity(): MaterialCategory {
    val entityColor = try {
        if (!this.color.isNullOrBlank()) CategoryColor.valueOf(this.color!!.uppercase())
        else CategoryColor.GRAY
    } catch (e: IllegalArgumentException) {
        CategoryColor.GRAY
    }

    var newCategory = MaterialCategory(
        id = 0,
        name = this.name,
        description = this.description,
        color = entityColor,
        active = true
    )
    return newCategory
}