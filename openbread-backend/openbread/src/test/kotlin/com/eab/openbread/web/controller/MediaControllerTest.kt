package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.JwtService
import com.eab.openbread.domain.service.FileService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.nio.file.Files
import java.nio.file.Path

@WebMvcTest(MediaController::class)
@AutoConfigureMockMvc(addFilters = false)
class MediaControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    private lateinit var jwtService: JwtService

    @MockkBean
    private lateinit var fileService: FileService

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `getMedia deberia retornar 200 OK y la imagen PNG cuando el archivo existe`(@TempDir tempDir: Path) {
        val entityDir = tempDir.resolve("users")
        Files.createDirectories(entityDir)

        val fakeImage = entityDir.resolve("avatar.png")
        val contentBytes = "contenido-falso-de-imagen-png".toByteArray()
        Files.write(fakeImage, contentBytes)

        every { fileService.rootLocation } returns tempDir

        // 3. Ejecución y aserciones
        mockMvc.get("/api/media/users/avatar.png")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.IMAGE_PNG) }
                content { bytes(contentBytes) }
            }
    }

    @Test
    fun `getMedia deberia retornar 404 Not Found cuando el archivo no existe en el sistema`(@TempDir tempDir: Path) {
        every { fileService.rootLocation } returns tempDir

        mockMvc.get("/api/media/users/archivo-fantasma.jpg")
            .andExpect {
                status { isNotFound() }
            }
    }
}