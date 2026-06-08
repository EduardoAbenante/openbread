package com.eab.openbread.web.controller

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
class MediaController {

    private val rootLocation = Paths.get("uploads")

    @GetMapping("/{entity}/{filename:.+}")
    fun getMedia(
        @PathVariable entity: String,
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

        } else  {
            return ResponseEntity.notFound().build()
        }
    }



}