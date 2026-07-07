package com.eab.openbread.domain.service

import com.eab.openbread.domain.ImageProfile
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.model.RawMaterial
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.domain.repository.RawMaterialRepository
import com.eab.openbread.web.dto.material.MaterialCreateDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.util.Optional

class RawMaterialServiceTest {

    private val materialRepository = mockk<RawMaterialRepository>()
    private val materialCategoryRepository = mockk<MaterialCategoryRepository>()
    private val mediaService = mockk<MediaService>()

    private val service = RawMaterialService(materialRepository, materialCategoryRepository, mediaService)

    // Entidades dummy para las simulaciones
    private val dummyCategory = MaterialCategory(id = 5L, name = "Harinas")
    private val dummyMaterial = RawMaterial(
        id = 100L,
        name = "Harina de Fuerza",
        description = "W300",
        category = dummyCategory,
        photoUrl = "/api/media/material/vieja.jpg",
        active = true
    )

    @Nested
    inner class CreateTests {

        @Test
        fun `createMaterial deberia guardar con éxito si la categoria existe y el nombre esta libre`() {
            val dto = MaterialCreateDTO(name = "Harina de Fuerza", categoryId = 5L, description = "W300")

            every { materialRepository.existsByName("Harina de Fuerza") } returns false
            every { materialCategoryRepository.findById(5L) } returns Optional.of(dummyCategory)
            every { materialRepository.save(any()) } returns dummyMaterial

            val id = service.createMaterial(dto)

            assertThat(id).isEqualTo(100L)
            verify(exactly = 1) { materialRepository.save(any()) }
        }

        @Test
        fun `createMaterial deberia lanzar excepcion si la categoria asignada no se encuentra`() {
            val dto = MaterialCreateDTO(name = "Masa Madre", categoryId = 99L, description = "Error")

            every { materialRepository.existsByName("Masa Madre") } returns false
            every { materialCategoryRepository.findById(99L) } returns Optional.empty()

            assertThrows<ResourceNotFoundException> {
                service.createMaterial(dto)
            }
            verify(exactly = 0) { materialRepository.save(any()) }
        }
    }

    @Nested
    inner class UpdatePhotoTests {

        @Test
        fun `updateUploadedPhoto deberia eliminar la foto antigua y guardar la nueva`() {
            val file = MockMultipartFile("file", "nueva.jpg", "image/jpeg", byteArrayOf(1, 2, 3))

            every { materialRepository.findById(100L) } returns Optional.of(dummyMaterial)
            every { mediaService.deleteMedia("/api/media/material/vieja.jpg") } returns Unit
            every { mediaService.processAndSaveImage(file, ImageProfile.MATERIAL) } returns "/api/media/material/nueva_procesada.jpg"
            every { materialRepository.save(dummyMaterial) } returns dummyMaterial

            val pathResult = service.updateUploadedPhoto(100L, file)

            assertThat(pathResult).isEqualTo("/api/media/material/nueva_procesada.jpg")
            assertThat(dummyMaterial.photoUrl).isEqualTo("/api/media/material/nueva_procesada.jpg")

            verify(exactly = 1) { mediaService.deleteMedia("/api/media/material/vieja.jpg") }
            verify(exactly = 1) { mediaService.processAndSaveImage(file, ImageProfile.MATERIAL) }
            verify(exactly = 1) { materialRepository.save(dummyMaterial) }
        }
    }

    @Nested
    inner class DeleteTests {

        @Test
        fun `deleteMaterial deberia cambiar active a false si estaba activo`() {
            dummyMaterial.active = true
            every { materialRepository.findById(100L) } returns Optional.of(dummyMaterial)
            every { materialRepository.save(dummyMaterial) } returns dummyMaterial

            service.deleteMaterial(100L)

            assertThat(dummyMaterial.active).isFalse()
            verify(exactly = 1) { materialRepository.save(dummyMaterial) }
        }

        @Test
        fun `deleteMaterial deberia lanzar IllegalArgumentException si ya estaba inactivo`() {
            dummyMaterial.active = false
            every { materialRepository.findById(100L) } returns Optional.of(dummyMaterial)

            val exception = assertThrows<IllegalArgumentException> {
                service.deleteMaterial(100L)
            }

            assertThat(exception.message).contains("is already active")
            verify(exactly = 0) { materialRepository.save(any()) }
        }
    }
}