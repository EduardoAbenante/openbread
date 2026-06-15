package com.eab.openbread.web.dto.material

import com.eab.openbread.domain.model.RawMaterial

class MaterialResponseDTO (
    var id: Long,
    var name: String,
    var description: String?,
    var categoryId: Long?,
    var categoryName: String?,
    var photoUrl: String?,
    var active: Boolean
)

fun toDTO(entity: RawMaterial): MaterialResponseDTO {
    var newDTO = MaterialResponseDTO(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        categoryId = entity.category?.id,
        categoryName = entity.category?.name,
        photoUrl = entity.photoUrl,
        active = entity.active
    )
    return newDTO
}