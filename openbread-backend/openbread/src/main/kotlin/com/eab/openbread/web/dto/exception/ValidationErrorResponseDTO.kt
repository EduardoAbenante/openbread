package com.eab.openbread.web.dto.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Standard structure for error validation in forms")
data class ValidationErrorResponseDTO (
    @Schema(example = "Validation Error")
   val message: String,
    @Schema(example = "Password doesn't pass validation standards")
   val errors : Map<String, String?>
)