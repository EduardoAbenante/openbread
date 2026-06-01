package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.UserService
import com.eab.openbread.web.dto.user.UserCreateDTO
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
}