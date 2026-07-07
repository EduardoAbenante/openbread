package com.eab.openbread.domain.service

import com.eab.openbread.domain.ImageProfile
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile

class MediaServiceTest {

    private val fileService = mockk<FileService>()
    private val mediaService = MediaService(fileService)

    // ¡Usamos directamente tu Enum real del dominio!
    private val testProfile = ImageProfile.AVATAR

    @Test
    fun `processAndSaveImage deberia redimensionar la imagen y guardarla a traves de FileService`() {
        // Generamos dinámicamente una imagen real de 1x1 píxeles en memoria
        val bufferedImage = java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB)
        val outputStream = java.io.ByteArrayOutputStream()
        javax.imageio.ImageIO.write(bufferedImage, "png", outputStream)
        val imagenRealBytes = outputStream.toByteArray()

        val mockFile = MockMultipartFile(
            "file",
            "test_avatar.png",
            "image/png",
            imagenRealBytes
        )

        val expectedUrl = "/api/media/avatar/random-uuid.png"
        every {
            fileService.saveFileFromBytes(any(), "test_avatar.png", "avatar")
        } returns expectedUrl

        val resultUrl = mediaService.processAndSaveImage(mockFile, testProfile)

        assertThat(resultUrl).isEqualTo(expectedUrl)

        verify(exactly = 1) {
            fileService.saveFileFromBytes(any(), "test_avatar.png", "avatar")
        }
    }

    @Test
    fun `processAndSaveImage deberia lanzar excepcion si el archivo Multipart esta vacio`() {
        val emptyFile = MockMultipartFile("file", "vacio.jpg", "image/jpeg", ByteArray(0))

        val exception = assertThrows<IllegalArgumentException> {
            mediaService.processAndSaveImage(emptyFile, testProfile)
        }

        assertThat(exception.message).isEqualTo("error.file.empty")
        verify(exactly = 0) { fileService.saveFileFromBytes(any(), any(), any()) }
    }

    @Test
    fun `deleteMedia deberia invocar el borrado en FileService si el path no es nulo`() {
        val targetPath = "/api/media/avatar/foto.jpg"
        every { fileService.deleteFile(targetPath) } returns Unit

        mediaService.deleteMedia(targetPath)

        verify(exactly = 1) { fileService.deleteFile(targetPath) }
    }

    @Test
    fun `deleteMedia no deberia hacer nada si el path suministrado es nulo`() {
        mediaService.deleteMedia(null)

        verify(exactly = 0) { fileService.deleteFile(any()) }
    }
}