package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class UserSpecificationTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()

        userRepository.save(
            User(
                nif = "12345678A",
                name = "Eduardo",
                surname = "Alvarez",
                email = "edu@openbread.com",
                password = "hashed_password_1",
                phone = "600111222",
                postalCode = "08221",
                role = Role.ADMIN,
                active = true
            )
        )
        userRepository.save(
            User(
                nif = "87654321B",
                name = "Maria",
                surname = "Gomez",
                email = "maria@pan.com",
                password = "hashed_password_2",
                phone = "611333444",
                postalCode = "08001",
                role = Role.USER,
                active = true
            )
        )
        userRepository.save(
            User(
                nif = "45678912C",
                name = "Carlos",
                surname = "Sanz",
                email = "carlos@bakery.es",
                password = "hashed_password_3",
                phone = "933444555",
                postalCode = "28001",
                role = Role.USER,
                active = false
            )
        )
    }

    @Nested
    inner class SmartSearchTests {

        @Test
        fun `deberia devolver todos los usuarios si el termino es nulo o vacio`() {
            val spec = UserSpecification.smartSearch(null)
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(3)
        }

        @Test
        fun `deberia buscar coincidencia parcial por NIF sin importar mayusculas`() {
            // Buscamos parte del NIF de Eduardo en minúsculas
            val spec = UserSpecification.smartSearch("345678a")
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Eduardo")
        }

        @Test
        fun `deberia buscar coincidencia parcial por nombre o apellido`() {
            val spec = UserSpecification.smartSearch("ma")
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(1)
        }

        @Test
        fun `deberia buscar coincidencia parcial por email`() {
            val spec = UserSpecification.smartSearch("bakery")
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Carlos")
        }

        @Test
        fun `deberia buscar coincidencia parcial por telefono o codigo postal`() {
            val spec = UserSpecification.smartSearch("08221")
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Eduardo")
        }

        @Test
        fun `deberia aplicar trim eliminando espacios en blanco innecesarios alrededor del termino`() {
            val spec = UserSpecification.smartSearch("  Maria  ")
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Maria")
        }
    }

    @Nested
    inner class ActiveStatusTests {

        @Test
        fun `deberia devolver todos los usuarios si el estado activo es nulo`() {
            val spec = UserSpecification.withActiveStatus(null)
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(3)
        }

        @Test
        fun `deberia filtrar unicamente los usuarios activos`() {
            val spec = UserSpecification.withActiveStatus(true)
            val result = userRepository.findAll(spec)

            assertThat(result).hasSize(2)
            assertThat(result.map { it.name }).containsExactlyInAnyOrder("Eduardo", "Maria")
        }

        @Test
        fun `deberia poder combinarse Smart Search y Estado Activo de forma conjunta`() {
            // Buscamos "ca" (Coincide con Eduardo Alva-rez- y -Ca-rlos Sanz)
            val searchSpec = UserSpecification.smartSearch("ca")
            // Pero filtramos para que SOLO devuelva los INACTIVOS (false)
            val activeSpec = UserSpecification.withActiveStatus(false)

            // Combinamos ambas especificaciones con un .and() de Spring Data JPA
            val combinedSpec = searchSpec.and(activeSpec)
            val result = userRepository.findAll(combinedSpec)

            // Solo Carlos cumple ambos criterios a la vez
            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Carlos")
        }
    }
}