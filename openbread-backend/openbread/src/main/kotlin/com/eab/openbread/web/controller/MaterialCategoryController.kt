package com.eab.openbread.web.controller

import com.eab.openbread.domain.service.MaterialCategoryService
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryCreateDTO
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryResponseDTO
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryUpdateDTO
import com.eab.openbread.web.openapi.ApiConflictError
import com.eab.openbread.web.openapi.ApiNotFoundError
import com.eab.openbread.web.openapi.ApiStandardErrors
import com.eab.openbread.web.openapi.ApiValidationError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/material-categories")
@Tag(name = "Material Categories", description = "APIs for managing material categories")
class MaterialCategoryController (
    private val materialCategoryService: MaterialCategoryService
) {

    @Operation(summary = "Create new Material Category", description = "Creates a new Material Category")
    @ApiResponse(responseCode = "200", description = "Category created successfully")
    @ApiValidationError
    @ApiConflictError
    @PostMapping
    fun createMaterialCategory(
        @Parameter(description = "Material Category to create", required = true)
        @Valid @RequestBody category: MaterialCategoryCreateDTO
    ): ResponseEntity<Long> {
        val newCategoryId = materialCategoryService.createMaterialCategory(category)
        return ResponseEntity.ok(newCategoryId)
    }

    @Operation(summary = "List material categories", description = "Retrieves a list of material categories")
    @ApiResponse(responseCode = "200", description = "List of material categories retrieved successfully")
    @GetMapping
fun listMaterialCategories(
        @Parameter(description = "Search term to filter material categories by name", required = false)
        @RequestParam(required = false) search: String?,

        @Parameter(description = "Filter material categories by active status", required = false)
        @RequestParam(required = false) active: Boolean?
): ResponseEntity<List<MaterialCategoryResponseDTO>> {
    val materialCategories = materialCategoryService.findMaterialCategories(search, active)
    return ResponseEntity.ok(materialCategories)

}

    @Operation(summary = "Update material category", description = "Updates a material category by its ID")
    @ApiResponse(responseCode = "200", description = "Material category updated successfully")
    @ApiValidationError
    @ApiConflictError
    @ApiStandardErrors
    @PostMapping("/{id}")
    fun updateCategory(
        @Parameter(description = "ID of the category to update", required = true)
        @PathVariable id: Long,
        @Parameter(description = "Category to update", required = true)
        @Valid @RequestBody updatedCategory: MaterialCategoryUpdateDTO
    ): ResponseEntity<Long> {
        val updatedId = materialCategoryService.updateMaterialCategory(id, updatedCategory)
        return ResponseEntity.ok(updatedId)
    }

    @Operation(summary = "Delete category", description = "Deletes a material category by its ID")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiValidationError
    @ApiNotFoundError
    @PostMapping("/{id}/delete")
    fun deleteCategory(
        @Parameter(description = "ID of the category to delete", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        materialCategoryService.deleteMaterialCategory(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Activate category", description = "Activates a deleted material category by its ID")
    @ApiResponse(responseCode = "200", description = "Category activated successfully")
    @ApiNotFoundError
    @PostMapping("/{id}/activate")
    fun activateCategory(
        @Parameter(description = "ID of the category to activate", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Long> {
        val activatedCategory = materialCategoryService.activateMaterialCategory(id)
        return ResponseEntity.ok(activatedCategory)
    }
}