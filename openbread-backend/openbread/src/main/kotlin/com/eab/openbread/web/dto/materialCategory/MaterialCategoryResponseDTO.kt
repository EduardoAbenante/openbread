package com.eab.openbread.web.dto.materialCategory

import com.eab.openbread.domain.model.MaterialCategory
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Material category response DTO.")
class MaterialCategoryResponseDTO(

    @field:Schema(
        description = "Category identifier.",
        example = "1",
        required = true
    )
    val id: Long,

    @field:Schema(
        description = "Category name.",
        example = "Bakery",
        required = true
    )
    val name: String,

    @field:Schema(
        description = "Category description.",
        example = "Categories related to bread and bakery products"
    )
    val description: String?,

    @field:Schema(
        description = "Indicates whether the category is active.",
        example = "true",
        required = true
    )
    val active: Boolean,

    @field:Schema(
        description = "Category color (string value of the CategoryColor enum).",
        example = "GRAY",
        required = true
    )
    val color: String,

    @field:Schema(
        description = "Category creation timestamp (as a string).",
        example = "2026-07-07T10:15:30Z"
    )
    val createdAt: String,

    @field:Schema(
        description = "Category last update timestamp (as a string).",
        example = "2026-07-10T08:22:10Z"
    )
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
