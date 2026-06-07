package com.eab.openbread.web.controller

import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.service.UserService
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.UserPasswordUpdateDTO
import com.eab.openbread.web.dto.user.UserResponseDTO
import com.eab.openbread.web.dto.user.UserRoleUpdateDTO
import com.eab.openbread.web.dto.user.UserUpdateDTO
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping()
    fun createUser(
        @Valid @RequestBody user: UserCreateDTO,
    ): ResponseEntity<Long> {
        val newUserId = userService.createUser(user)
        return ResponseEntity.ok(newUserId)
    }

    @GetMapping("/users")
    fun findUsers(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) active: Boolean?
    ): ResponseEntity<List<UserResponseDTO>> {
        val users = userService.findUsers(search, active)
        return ResponseEntity.ok(users)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody  dto: UserUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUser(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @PutMapping("/{id}/role")
    fun updateUserRole(
        @PathVariable id: Long,
        @Valid @RequestBody  dto: UserRoleUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUserRole(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @PutMapping("/{id}/password")
    fun updateUserPassword(
        @PathVariable id: Long,
        @Valid @RequestBody  dto: UserPasswordUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUserPassword(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @PostMapping("/{id}/photo")
    fun updateUserAvatar(
        @PathVariable id: Long,
        @RequestParam("photoFile") file: MultipartFile,
    ): ResponseEntity<Map<String, String?>>{
        val avatarUrl = userService.updateUploadedAvatar(id, file)
        return ResponseEntity.ok(mapOf("avatarUrl" to avatarUrl))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val currentUserEmail = authentication.principal as String
        userService.deleteUser(id, currentUserEmail)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/activate")
    fun activateUser(
        @PathVariable id: Long
    ): ResponseEntity<Long> {
        val activatedUser = userService.activateUser(id)
        return ResponseEntity.ok(activatedUser)
    }


}