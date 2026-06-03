package com.eab.openbread.web.controller

import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.service.UserService
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.UserPasswordUpdateDTO
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
        @RequestParam(required = false) nif: String?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) surname: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) postalCode: String?,
        @RequestParam(required = false) active: Boolean?
    ): ResponseEntity<List<User>> {
        val users = userService.findUsers(nif, name, surname, email, phone, postalCode, active)
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

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val currentUserId = authentication.principal as Long
        userService.deleteUser(id, currentUserId)
        return ResponseEntity.noContent().build()
    }


}