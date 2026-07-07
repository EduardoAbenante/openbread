package com.eab.openbread.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Paths

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media", description = "Media file retrieval")
class MediaController {

    private val rootLocation = Paths.get("uploads")

    @Operation(
        summary = "Get media file",
        description = "Returns a media file from the uploads directory for a given entity and filename."
    )
    @ApiResponse(responseCode = "200", description = "Media file retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Media file not found")
    @GetMapping("/{entity}/{filename:.+}")
    fun getMedia(
        @Parameter(description = "Entity folder name inside uploads.", required = true, example = "users")
        @PathVariable entity: String,

        @Parameter(description = "Filename to retrieve (supports dots).", required = true, example = "avatar.png")
        @PathVariable filename: String
    ): ResponseEntity<Resource> {
        val file = rootLocation.resolve(entity).resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            val contentType = when (filename.substringAfterLast(".", "").lowercase()) {
                "png" -> MediaType.IMAGE_PNG
                "jpg", "jpeg" -> MediaType.IMAGE_JPEG
                "pdf" -> MediaType.APPLICATION_PDF
                else -> MediaType.APPLICATION_OCTET_STREAM
            }

            return ResponseEntity.ok()
                .contentType(contentType)
                .body(resource)
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}
