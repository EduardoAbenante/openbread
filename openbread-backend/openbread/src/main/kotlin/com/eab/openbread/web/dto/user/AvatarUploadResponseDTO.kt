package com.eab.openbread.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Avatar upload response DTO.")
data class AvatarUploadResponseDTO(

    @field:Schema(
        description = "URL of the uploaded avatar image. Can be null if no avatar was uploaded.",
        example = "https://example.com/avatars/user123.png"
    )
    val avatarUrl: String?
)
