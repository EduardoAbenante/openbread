package com.eab.openbread.web.dto.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Standard structure for error response")
data class ErrorResponseDTO (
    @Schema(example = "Resource not found")
    val message: String?
)