package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.model.RawMaterial
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.domain.repository.RawMaterialRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.data.jpa.domain.Specification
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class MaterialSpecificationTest {

    @Autowired
    private lateinit var repository: RawMaterialRepository

    @Autowired
    private lateinit var categoryRepository: MaterialCategoryRepository

    @BeforeEach
    fun setUp() {
        // 1. Limpiamos en orden inverso por las claves foráneas
        repository.deleteAll()
        categoryRepository.deleteAll()

        // 2. Creamos las categorías
        val cat1 = categoryRepository.save(MaterialCategory(name = "Harinas y Derivados"))
        val cat2 = categoryRepository.save(MaterialCategory(name = "Semillas y Granos"))

        // 3. Insertamos los datos de prueba usando los objetos creados
        repository.save(RawMaterial(name = "Harina de Trigo", category = cat1, active = true))
        repository.save(RawMaterial(name = "Centeno Orgánico", category = cat2, active = true))
        repository.save(RawMaterial(name = "Sal Marina", category = cat1, active = false))
    }

    @Nested
    inner class SmartSearchTests {

        @Test
        fun `deberia devolver todos los materiales si el termino de busqueda es nulo o vacio`() {
            val spec = MaterialSpecification.smartSearch(null)
            val result = repository.findAll(spec)

            assertThat(result).hasSize(3)
        }

        @Test
        fun `deberia buscar por coincidencia parcial en el nombre sin importar mayusculas`() {
            // Buscamos "cEnTe" para probar el lowerCase y el LIKE
            val spec = MaterialSpecification.smartSearch("cEnTe")
            val result = repository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Centeno Orgánico")
        }

        @Test
        fun `deberia buscar por ID de categoria si el termino es un numero valido`() {
            // Obtenemos el ID real generado por H2 (que guardamos en el setUp)
            // Para poder acceder a 'cat1', asegúrate de declararlo como propiedad de la clase o recuperarlo aquí.
            val idRealCategoria = categoryRepository.findAll().first { it.name == "Harinas y Derivados" }.id

            // 2. Pasamos ese ID real como String al buscador
            val spec = MaterialSpecification.smartSearch(idRealCategoria.toString())
            val result = repository.findAll(spec)

            // 3. Ahora la aserción pasará sin importar qué número de ID haya asignado la BD
            assertThat(result).hasSize(2)
            assertThat(result.map { it.name }).containsExactlyInAnyOrder("Harina de Trigo", "Sal Marina")
        }

        @Test
        fun `no deberia romper la query ni fallar si el termino de busqueda es texto puro y no puede convertirse a id`() {
            // Probamos que "Harina" (que no es numérico) solo busque por nombre y no lance NumberFormatException en BD
            val spec = MaterialSpecification.smartSearch("Harina")

            val result = repository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Harina de Trigo")
        }

        @Test
        fun `deberia devolver una lista vacia si el termino no coincide con ningun criterio`() {
            // Probamos un término que ni es ID válido existente ni coincide con nombres
            val spec = MaterialSpecification.smartSearch("Levadura Inexistente")
            val result = repository.findAll(spec)

            assertThat(result).isEmpty()
        }
    }
}