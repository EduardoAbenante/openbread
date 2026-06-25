package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.CategoryColor

class MaterialCategoryUpdateDTO (
    val name: String?,
    val description: String?,
    val color: CategoryColor?
)
