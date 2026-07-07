package com.eab.openbread.web.dto.material

import com.eab.openbread.domain.model.RawMaterial
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Material response DTO including category details and metadata.")
class MaterialResponseDTO(

    @field:Schema(
        description = "Material identifier.",
        example = "1",
        required = true
    )
    var id: Long,

    @field:Schema(
        description = "Material name.",
        example = "Bread",
        required = true
    )
    var name: String,

    @field:Schema(
        description = "Material description.",
        example = "Plain white bread"
    )
    var description: String?,

    @field:Schema(
        description = "ID of the category associated with the material.",
        example = "2"
    )
    var categoryId: Long?,

    @field:Schema(
        description = "Name of the category associated with the material.",
        example = "Bakery"
    )
    var categoryName: String?,

    @field:Schema(
        description = "URL of the material photo.",
        example = "https://example.com/photos/bread.jpg"
    )
    var photoUrl: String?,

    @field:Schema(
        description = "Indicates whether the material is active.",
        example = "true",
        required = true
    )
    var active: Boolean,

    @field:Schema(
        description = "Material creation timestamp (as a string).",
        example = "2026-07-07T10:15:30Z"
    )
    var createdAt: String? = null,

    @field:Schema(
        description = "Material last update timestamp (as a string).",
        example = "2026-07-10T08:22:10Z"
    )
    var updatedAt: String? = null
)

fun toDTO(entity: RawMaterial): MaterialResponseDTO {
    return MaterialResponseDTO(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        categoryId = entity.category?.id,
        categoryName = entity.category?.name,
        photoUrl = entity.photoUrl,
        active = entity.active,
        createdAt = entity.createdAt?.toString(),
        updatedAt = entity.updatedAt?.toString(),
    )
}
