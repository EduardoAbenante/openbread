package com.eab.openbread.web.openapi

import com.eab.openbread.web.dto.exception.ErrorResponseDTO
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "401",
    description = "Incorrect credentials or invalid token.",
    content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
)
annotation class ApiUnauthorizedError