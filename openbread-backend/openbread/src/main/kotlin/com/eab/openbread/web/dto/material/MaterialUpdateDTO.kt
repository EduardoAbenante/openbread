package com.eab.openbread.web.dto.material

import jakarta.validation.constraints.Size

data class MaterialUpdateDTO (
    @field:Size(min = 6, max = 100, message = "{val.material.name.size}")
    val name: String,
    @field:Size(max = 100, message = "{val.material.description.size}")
    val description: String?,
    val categoryId: Long?,
)