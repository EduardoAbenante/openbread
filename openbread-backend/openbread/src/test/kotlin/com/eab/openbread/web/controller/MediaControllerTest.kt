package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.JwtService
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.servlet.function.RequestPredicates.contentType
import java.nio.file.Files
import java.nio.file.Path

@WebMvcTest(MediaController::class)
@AutoConfigureMockMvc(addFilters = false)
class MediaControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    private lateinit var jwtService: JwtService

    @Test
    fun `getMedia deberia retornar 200 OK y la imagen PNG cuando el archivo existe`() {
        val baseUploads = Path.of("uploads")
        val usersDir = baseUploads.resolve("users")

        Files.createDirectories(usersDir)
        val fakeImage = usersDir.resolve("avatar.png")
        Files.writeString(fakeImage, "contenido-falso-de-imagen-png")

        try {
            mockMvc.get("/api/media/users/avatar.png")
                .andExpect {
                    status { isOk() }
                    contentType(MediaType.IMAGE_PNG)
                    content().string("contenido-falso-de-imagen-png")
                }
        } finally {
            baseUploads.toFile().deleteRecursively()
        }
    }

    @Test
    fun `getMedia deberia retornar 404 Not Found cuando el archivo no existe en el sistema`() {
        mockMvc.get("/api/media/users/archivo-fantasma.jpg")
            .andExpect {
                status { isNotFound() }
            }
    }
}