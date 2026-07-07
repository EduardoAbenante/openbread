package com.eab.openbread.domain.service

import com.eab.openbread.domain.ImageProfile
import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.repository.UserRepository
import com.eab.openbread.web.dto.user.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val mediaService = mockk<MediaService>()

    private val userService = UserService(userRepository, passwordEncoder, mediaService)

    private val dummyUser = User(
        id = 42L,
        nif = "12345678A",
        name = "Edu",
        surname = "Alvarez",
        email = "edu@openbread.com",
        password = "hashed_password",
        role = Role.ADMIN,
        photoUrl = "/api/media/avatar/viejo.jpg",
        active = true
    )

    @Nested
    inner class CreateUserTests {

        @Test
        fun `createUser deberia guardar el usuario si el NIF y el Email estan libres`() {
            val dto = UserCreateDTO(
                nif = "12345678A",
                name = "Edu",
                surname = "Alvarez",
                email = "edu@openbread.com",
                password = "Openbread2026!",
                phone = null,
                postalCode = null,
                role = Role.ADMIN
            )

            every { userRepository.existsByNif("12345678A") } returns false
            every { userRepository.existsByEmail("edu@openbread.com") } returns false
            every { passwordEncoder.encode(dto.password) } returns "hashed_password"
            every { userRepository.save(any()) } returns dummyUser

            val generatedId = userService.createUser(dto)

            assertThat(generatedId).isEqualTo(42L)
            verify(exactly = 1) { userRepository.save(any()) }
        }

        @Test
        fun `createUser deberia lanzar excepcion si el NIF ya existe`() {
            val dto = UserCreateDTO(
                nif = "12345678A",
                name = "Edu",
                surname = "Alvarez",
                email = "edu@openbread.com",
                password = "Openbread2026!",
                phone = null,
                postalCode = null,
                role = Role.ADMIN
            )

            every { userRepository.existsByNif("12345678A") } returns true

            val exception = assertThrows<DuplicateResourceException> {
                userService.createUser(dto)
            }
            assertThat(exception.message).isEqualTo("error.user.nif_exists")
            verify(exactly = 0) { userRepository.save(any()) }
        }
    }

    @Nested
    inner class UpdateAvatarTests {

        @Test
        fun `updateUploadedAvatar deberia borrar el antiguo avatar del disco y procesar el nuevo`() {
            val mockFile = MockMultipartFile("avatar", "nuevo.png", "image/png", byteArrayOf(1, 2, 3))

            every { userRepository.findById(42L) } returns Optional.of(dummyUser)
            every { mediaService.deleteMedia("/api/media/avatar/viejo.jpg") } returns Unit
            every { mediaService.processAndSaveImage(mockFile, ImageProfile.AVATAR) } returns "/api/media/avatar/nuevo_procesado.png"
            every { userRepository.save(dummyUser) } returns dummyUser

            val finalPath = userService.updateUploadedAvatar(42L, mockFile)

            assertThat(finalPath).isEqualTo("/api/media/avatar/nuevo_procesado.png")
            assertThat(dummyUser.photoUrl).isEqualTo("/api/media/avatar/nuevo_procesado.png")

            verify(exactly = 1) { mediaService.deleteMedia("/api/media/avatar/viejo.jpg") }
            verify(exactly = 1) { mediaService.processAndSaveImage(mockFile, ImageProfile.AVATAR) }
        }
    }

    @Nested
    inner class DeleteUserTests {

        @Test
        fun `deleteUser deberia desactivar al usuario de forma logica si no es el mismo`() {
            dummyUser.active = true
            every { userRepository.findById(42L) } returns Optional.of(dummyUser)
            every { userRepository.save(dummyUser) } returns dummyUser

            // El usuario administrador de la sesión es "otro@openbread.com", no el dueño de la cuenta
            userService.deleteUser(42L, "otro@openbread.com")

            assertThat(dummyUser.active).isFalse()
            verify(exactly = 1) { userRepository.save(dummyUser) }
        }

        @Test
        fun `deleteUser deberia lanzar excepcion si el usuario intenta borrarse a si mismo`() {
            every { userRepository.findById(42L) } returns Optional.of(dummyUser)

            // El email coincide con el de dummyUser ("edu@openbread.com")
            val exception = assertThrows<IllegalArgumentException> {
                userService.deleteUser(42L, "edu@openbread.com")
            }

            assertThat(exception.message).isEqualTo("error.user.delete_self")
            verify(exactly = 0) { userRepository.save(any()) }
        }
    }

    @Nested
    inner class UpdatePasswordAndRoleTests {

        @Test
        fun `updateUserPassword deberia hashear la nueva clave antes de guardarla`() {
            val dto = UserPasswordUpdateDTO(password = "new_raw_pass")
            every { userRepository.findById(42L) } returns Optional.of(dummyUser)
            every { passwordEncoder.encode("new_raw_pass") } returns "new_hashed_pass"
            every { userRepository.save(dummyUser) } returns dummyUser

            val id = userService.updateUserPassword(42L, dto)

            assertThat(id).isEqualTo(42L)
            assertThat(dummyUser.password).isEqualTo("new_hashed_pass")
        }

        @Test
        fun `updateUserRole deberia cambiar el rol del usuario`() {
            val dto = UserRoleUpdateDTO(role = Role.USER)
            every { userRepository.findById(42L) } returns Optional.of(dummyUser)
            every { userRepository.save(dummyUser) } returns dummyUser

            val id = userService.updateUserRole(42L, dto)

            assertThat(id).isEqualTo(42L)
            assertThat(dummyUser.role).isEqualTo(Role.USER)
        }
    }
}