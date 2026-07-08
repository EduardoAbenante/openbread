package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.UserService
import com.eab.openbread.web.dto.user.AvatarUploadResponseDTO
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.UserPasswordUpdateDTO
import com.eab.openbread.web.dto.user.UserResponseDTO
import com.eab.openbread.web.dto.user.UserRoleUpdateDTO
import com.eab.openbread.web.dto.user.UserUpdateDTO
import com.eab.openbread.web.openapi.ApiConflictError
import com.eab.openbread.web.openapi.ApiNotFoundError
import com.eab.openbread.web.openapi.ApiStandardErrors
import com.eab.openbread.web.openapi.ApiValidationError
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
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.Parameter

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details.")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiValidationError
    @ApiConflictError
    @PostMapping
    fun createUser(
        @Valid @RequestBody user: UserCreateDTO,
    ): ResponseEntity<Long> {
        val newUserId = userService.createUser(user)
        return ResponseEntity.ok(newUserId)
    }

    @Operation(summary = "List users", description = "Retrieves a list of users, optionally filtered by search criteria and active status.")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GetMapping
    fun listUsers(
        @Parameter(description = "Search term to filter users by name or email", required = false)
        @RequestParam(required = false) search: String?,

        @Parameter(description = "Filter users by active status", required = false)
        @RequestParam(required = false) active: Boolean?
    ): ResponseEntity<List<UserResponseDTO>> {
        val users = userService.findUsers(search, active)
        return ResponseEntity.ok(users)
    }

    @Operation(summary = "Update user", description = "Updates a user by their unique ID.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiStandardErrors
    @ApiConflictError
    @PutMapping("/{id}")
    fun updateUser(
        @Parameter(description = "ID of the user to update", required = true) @PathVariable id: Long, @Valid @RequestBody dto: UserUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUser(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @Operation(summary = "Update user role", description = "Updates the role of a user by their unique ID.")
    @ApiResponse(responseCode = "200", description = "User role updated successfully")
    @ApiStandardErrors
    @PutMapping("/{id}/role")
    fun updateUserRole(
        @Parameter(description = "ID of the user to update role", required = true) @PathVariable id: Long,
        @Valid @RequestBody dto: UserRoleUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUserRole(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @Operation(summary = "Update user password", description = "Updates the password of a user by their unique ID.")
    @ApiResponse(responseCode = "200", description = "User password updated successfully")
    @ApiStandardErrors
    @PutMapping("/{id}/password")
    fun updateUserPassword(
        @Parameter(description = "ID of the user to update password", required = true) @PathVariable id: Long,
        @Valid @RequestBody dto: UserPasswordUpdateDTO,
    ): ResponseEntity<Long> {
        val updatedId = userService.updateUserPassword(id, dto)
        return ResponseEntity.ok(updatedId)
    }

    @Operation(summary = "Update user avatar", description = "Updates the avatar of a user by their unique ID.")
    @ApiResponse(responseCode = "200", description = "User avatar updated successfully")
    @ApiNotFoundError
    @PostMapping("/{id}/avatar")
    fun updateUserAvatar(
        @Parameter(description = "ID of the user to update avatar", required = true) @PathVariable id: Long,
        @Parameter(description = "Avatar file to upload", required = true) @RequestParam("avatarFile") file: MultipartFile,
    ): ResponseEntity<AvatarUploadResponseDTO> {
        val avatarUrl = userService.updateUploadedAvatar(id, file)
        return ResponseEntity.ok(AvatarUploadResponseDTO(avatarUrl = avatarUrl))
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their unique ID. Requires authentication.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiValidationError
    @ApiNotFoundError
    @DeleteMapping("/{id}")
    fun deleteUser(
        @Parameter(description = "ID of the user to delete", required = true) @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val currentUserEmail = authentication.principal as String
        userService.deleteUser(id, currentUserEmail)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Activate user", description = "Activates a user by their unique ID.")
    @ApiResponse(responseCode = "200", description = "User activated successfully")
    @ApiNotFoundError
    @PutMapping("/{id}/activate")
    fun activateUser(
        @Parameter(description = "ID of the user to activate", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Long> {
        val activatedUser = userService.activateUser(id)
        return ResponseEntity.ok(activatedUser)
    }
}