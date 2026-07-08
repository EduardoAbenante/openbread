package com.eab.openbread.web.openapi

import com.eab.openbread.web.dto.exception.ValidationErrorResponseDTO
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "400",
    description = "Validation failure.",
    content = [Content(schema = Schema(implementation = ValidationErrorResponseDTO::class))]
)
annotation class ApiValidationError