package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.model.CategoryColor
import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryCreateDTO
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryUpdateDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException
import java.util.Optional

class MaterialCategoryServiceTest {

    private val repository = mockk<MaterialCategoryRepository>()
    private val service = MaterialCategoryService(repository)

    private val dummyCategory = MaterialCategory(
        id = 10L,
        name = "Harinas",
        description = "Harinas de todo tipo",
        color = CategoryColor.GRAY,
        active = true
    )

    @Nested
    inner class CreateTests {

        @Test
        fun `createMaterialCategory deberia guardar exitosamente y retornar el ID`() {
            val dto = MaterialCategoryCreateDTO(name = "Harinas", description = "Harinas de todo tipo", color = "BLUE")

            every { repository.existsByName("Harinas") } returns false
            every { repository.save(any()) } returns dummyCategory

            val id = service.createMaterialCategory(dto)

            assertThat(id).isEqualTo(10L)
            verify(exactly = 1) { repository.save(any()) }
        }

        @Test
        fun `createMaterialCategory deberia lanzar excepcion si el nombre ya existe`() {
            val dto = MaterialCategoryCreateDTO(name = "Harinas", description = "Duplicada", color = "RED")

            every { repository.existsByName("Harinas") } returns true

            val exception = assertThrows<DuplicateResourceException> {
                service.createMaterialCategory(dto)
            }

            assertThat(exception.message).isEqualTo("error.material_category.name_exists")
            verify(exactly = 0) { repository.save(any()) }
        }

        @Test
        fun `createMaterialCategory deberia lanzar excepcion inesperada ante violacion de restriccion de BD`() {
            val dto = MaterialCategoryCreateDTO(name = "Harinas", description = "Error BD", color = "GREEN")

            every { repository.existsByName("Harinas") } returns false
            every { repository.save(any()) } throws DataIntegrityViolationException("Constraint violation")

            val exception = assertThrows<DuplicateResourceException> {
                service.createMaterialCategory(dto)
            }

            assertThat(exception.message).isEqualTo("error.unexpected")
        }
    }

    @Nested
    inner class UpdateTests {

        @Test
        fun `updateMaterialCategory deberia modificar los campos y retornar el ID`() {
            val dto = MaterialCategoryUpdateDTO(name = "Harinas Premium", description = "Nueva desc", color = CategoryColor.PURPLE)

            every { repository.findById(10L) } returns Optional.of(dummyCategory)
            every { repository.save(any()) } returns dummyCategory

            val id = service.updateMaterialCategory(10L, dto)

            assertThat(id).isEqualTo(10L)
            assertThat(dummyCategory.name).isEqualTo("Harinas Premium")
            assertThat(dummyCategory.description).isEqualTo("Nueva desc")
            assertThat(dummyCategory.color).isEqualTo(CategoryColor.PURPLE)
        }

        @Test
        fun `updateMaterialCategory deberia lanzar excepcion si la categoria no existe`() {
            val dto = MaterialCategoryUpdateDTO(name = "Inexistente", description = null, color = null)
            every { repository.findById(99L) } returns Optional.empty()

            val exception = assertThrows<DuplicateResourceException> {
                service.updateMaterialCategory(99L, dto)
            }

            assertThat(exception.message).isEqualTo("error.material_category.not_found")
        }
    }

    @Nested
    inner class DeleteTests {

        @Test
        fun `deleteMaterialCategory deberia hacer un borrado logico pasando active a false`() {
            dummyCategory.active = true
            every { repository.findById(10L) } returns Optional.of(dummyCategory)
            every { repository.save(any()) } returns dummyCategory

            service.deleteMaterialCategory(10L)

            assertThat(dummyCategory.active).isFalse()
            verify(exactly = 1) { repository.save(dummyCategory) }
        }
    }

    @Nested
    inner class ActivateTests {

        @Test
        fun `activateMaterialCategory deberia pasar active a true si estaba inactiva`() {
            val inactiveCategory = MaterialCategory(id = 10L, name = "Sal", color = CategoryColor.YELLOW, active = false)

            every { repository.findById(10L) } returns Optional.of(inactiveCategory)
            every { repository.save(any()) } returns inactiveCategory

            val id = service.activateMaterialCategory(10L)

            assertThat(id).isEqualTo(10L)
            assertThat(inactiveCategory.active).isTrue()
        }

        @Test
        fun `activateMaterialCategory deberia lanzar IllegalArgumentException si ya estaba activa`() {
            dummyCategory.active = true
            every { repository.findById(10L) } returns Optional.of(dummyCategory)

            val exception = assertThrows<IllegalArgumentException> {
                service.activateMaterialCategory(10L)
            }

            assertThat(exception.message).contains("is already active")
        }
    }
}