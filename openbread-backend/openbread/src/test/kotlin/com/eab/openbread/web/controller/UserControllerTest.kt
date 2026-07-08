package com.eab.openbread.web.controller

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.service.JwtService
import com.eab.openbread.domain.service.UserService
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.UserResponseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.jacksonObjectMapper

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var jwtService: JwtService

    @Test
    fun `createUser deberia retornar 200 OK y el ID asignado cuando el DTO es valido`() {
        val userDTO = UserCreateDTO(
            nif = "12345678A",
            name = "Pepe",
            surname = "Perez",
            email = "pepe@openbread.com",
            password = "SecurePassword123@",
            phone = "612345678",
            postalCode = "28013",
            role = Role.USER
        )
        val expectedId = 1L

        every { userService.createUser(any()) } returns expectedId

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userDTO)
        }.andExpect {
            status { isOk() }
            content().string(expectedId.toString())
        }

        verify(exactly = 1) { userService.createUser(userDTO) }
    }

    @Test
    fun `listUsers deberia retornar una lista de usuarios de forma exitosa`() {
        val mockUsers = listOf(
            UserResponseDTO(
                id = 1L,
                nif = "12345678A",
                name = "Pepe",
                surname = "Perez",
                email = "pepe@openbread.com",
                phone = "612345678",
                postalCode = "28013",
                role = Role.USER,
                active = true,
                photoUrl = null
            )
        )

        every { userService.findUsers("Pepe", true) } returns mockUsers

        mockMvc.get("/api/users") {
            param("search", "Pepe")
            param("active", "true")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].id") { value(1L) }
            jsonPath("$[0].name") { value("Pepe") }
            jsonPath("$[0].nif") { value("12345678A") }
        }

        verify(exactly = 1) { userService.findUsers("Pepe", true) }
    }

    @Test
    fun `updateUserAvatar deberia aceptar un archivo multipart y retornar la URL de destino`() {
        val userId = 1L
        val fakeAvatarUrl = "uploads/users/avatar-1.png"

        val mockFile = MockMultipartFile(
            "avatarFile",
            "avatar.png",
            MediaType.IMAGE_PNG_VALUE,
            "contenido-binario-falso".toByteArray()
        )

        every { userService.updateUploadedAvatar(userId, any()) } returns fakeAvatarUrl

        mockMvc.multipart("/api/users/$userId/avatar") {
            file(mockFile)
        }.andExpect {
            status { isOk() }
            jsonPath("$.avatarUrl") { value(fakeAvatarUrl) }
        }

        verify(exactly = 1) { userService.updateUploadedAvatar(userId, any()) }
    }

    @Test
    fun `deleteUser deberia retornar 204 No Content y procesar la baja con el correo autenticado`() {
        val userId = 5L
        val emailUser = "admin@openbread.com"

        val fakeAuthentication = UsernamePasswordAuthenticationToken(
            emailUser,
            null,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )

        every { userService.deleteUser(userId, emailUser) } returns Unit

        mockMvc.delete("/api/users/$userId") {
            principal = fakeAuthentication
        }.andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { userService.deleteUser(userId, emailUser) }
    }

    @Test
    fun `listUsers deberia ser capturado por el GlobalExceptionHandler y retornar 404 cuando el recurso no existe`() {
        every { userService.findUsers(any(), any()) } throws ResourceNotFoundException("error.user.not_found")

        mockMvc.get("/api/users")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { exists() }
            }
    }

    @Test
    fun `createUser deberia retornar 409 Conflict si el usuario ya existe`() {
        val userDTO = UserCreateDTO(
            nif = "12345678A", name = "Pepe", surname = "Perez",
            email = "pepe@openbread.com", password = "SecurePassword123@",
            phone = null, postalCode = null, role = Role.USER
        )

        every { userService.createUser(any()) } throws DuplicateResourceException("error.user.duplicate")

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userDTO)
        }.andExpect {
            status { isConflict() }
            jsonPath("$.message") { exists() }
        }
    }
}