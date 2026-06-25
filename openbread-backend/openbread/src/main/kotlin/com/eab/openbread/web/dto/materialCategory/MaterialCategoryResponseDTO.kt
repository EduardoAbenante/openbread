package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.MaterialCategory

class MaterialCategoryResponseDTO (
    val id: Long,
    val name: String,
    val description: String?,
    val active: Boolean,
    val color: String,
    val createdAt: String,
    val updatedAt: String?
)

fun toDTO(entity: MaterialCategory): MaterialCategoryResponseDTO {
    return MaterialCategoryResponseDTO(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        active = entity.active,
        color = entity.color.toString(),
        createdAt = entity.createdAt.toString(),
        updatedAt = entity.updatedAt?.toString()
    )

}