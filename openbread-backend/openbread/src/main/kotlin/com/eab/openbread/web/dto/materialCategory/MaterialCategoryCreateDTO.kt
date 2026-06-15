package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.BaseAuditEntity
import com.eab.openbread.domain.model.MaterialCategory
import jakarta.validation.constraints.Size

class MaterialCategoryCreateDTO (
    @field:Size(min = 6, max = 100, message = "{val.materialCategory.name.size}")
    var name: String,
    @field:Size(max = 100, message = "{val.materialCategory.description.size}")
    var description: String?
): BaseAuditEntity()

fun MaterialCategoryCreateDTO.toEntity(): MaterialCategory {
    var newCategory = MaterialCategory(
        id = 0,
        name = this.name,
        description = this.description,
        active = true
    )
    return newCategory
}