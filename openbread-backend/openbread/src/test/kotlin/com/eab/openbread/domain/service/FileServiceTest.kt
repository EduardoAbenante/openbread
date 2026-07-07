package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.ResourceNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import java.nio.file.Files
import java.nio.file.Path

class FileServiceTest {

    private lateinit var fileService: FileService

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        fileService = FileService()
        ReflectionTestUtils.setField(fileService, "rootLocation", tempDir)
    }

    @Test
    fun `saveFile deberia guardar el archivo correctamente y retornar la URL virtual`() {
        val file = MockMultipartFile(
            "file",
            "avatar.png",
            "image/png",
            "contenido-de-imagen-dummy".toByteArray()
        )

        val resultUrl = fileService.saveFile(file, "users")

        assertThat(resultUrl).startsWith("/api/media/users/")
        assertThat(resultUrl).endsWith(".png")


        val physicalFileName = resultUrl.removePrefix("/api/media/users/")
        val expectedFilePath = tempDir.resolve("users").resolve(physicalFileName)
        assertThat(Files.exists(expectedFilePath)).isTrue()
    }

    @Test
    fun `saveFile deberia lanzar excepcion si el archivo esta vacio`() {
        val emptyFile = MockMultipartFile("file", "vacio.txt", "text/plain", ByteArray(0))

        val exception = assertThrows<ResourceNotFoundException> {
            fileService.saveFile(emptyFile, "products")
        }
        assertThat(exception.message).isEqualTo("error.file.empty")
    }

    @Test
    fun `saveFile deberia lanzar excepcion si el Content Type no esta permitido`() {
        val maliciousFile = MockMultipartFile("file", "virus.exe", "application/x-msdownload", "bytes".toByteArray())

        val exception = assertThrows<IllegalArgumentException> {
            fileService.saveFile(maliciousFile, "products")
        }
        assertThat(exception.message).isEqualTo("error.file.type_not_allowed")
    }


    @Test
    fun `saveFileFromBytes deberia escribir los bytes en disco y retornar la URL`() {
        val bytes = "documento-pdf-en-bytes".toByteArray()

        val resultUrl = fileService.saveFileFromBytes(bytes, "informe.pdf", "documents")

        assertThat(resultUrl).startsWith("/api/media/documents/")
        assertThat(resultUrl).endsWith(".pdf")

        val physicalFileName = resultUrl.removePrefix("/api/media/documents/")
        val expectedFilePath = tempDir.resolve("documents").resolve(physicalFileName)
        assertThat(Files.exists(expectedFilePath)).isTrue()
    }



    @Test
    fun `deleteFile deberia borrar el archivo si existe fisicamente`() {
        val entityFolder = tempDir.resolve("materials")
        Files.createDirectories(entityFolder)
        val fileToKill = entityFolder.resolve("harina.jpg")
        Files.write(fileToKill, "datos".toByteArray())

        assertThat(Files.exists(fileToKill)).isTrue()

        val virtualPath = "/api/media/materials/harina.jpg"
        fileService.deleteFile(virtualPath)

        assertThat(Files.exists(fileToKill)).isFalse()
    }
}