package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryCreateDTO
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryResponseDTO
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryUpdateDTO
import com.eab.openbread.web.dto.materialCategory.toDTO
import com.eab.openbread.web.dto.materialCategory.toEntity
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class MaterialCategoryService(
    private val MaterialCategoryRepository: MaterialCategoryRepository
) {
    //Create Read Update Delete

    private val logger = LoggerFactory.getLogger(MaterialCategoryService::class.java)


    fun createMaterialCategory(dto: MaterialCategoryCreateDTO): Long {
        logger.info("Creating material category: ${dto.name}")

        if (MaterialCategoryRepository.existsByName(dto.name)) {
            logger.warn("Material category creation failed: Name already in use (${dto.name})")
            throw DuplicateResourceException("error.material_category.name_exists")
        }

        try{
            val newCategory = dto.toEntity()
            val savedCategory = MaterialCategoryRepository.save(newCategory)
            return savedCategory.id
        } catch (e: DataIntegrityViolationException){
            logger.error("Material category creation failed due to database constraint violation", e)
            throw DuplicateResourceException("error.unexpected")
        }
    }

    fun getAllMaterialCategories(): List<MaterialCategoryResponseDTO> {
        logger.info("Fetching all material categories")
        return MaterialCategoryRepository.findAll().map { toDTO(it) }
    }

    fun updateMaterialCategory(id: Long, dto: MaterialCategoryUpdateDTO): Long {
        logger.info("Updating material category: ${dto.name}")
        var matCategory = MaterialCategoryRepository.findById(id)
            .orElseThrow{
                logger.warn("Material category update failed: categoryId=$id not found")
                DuplicateResourceException("error.material_category.not_found")
            }

        dto.name?.let { matCategory.name = it }
        dto.description?.let { matCategory.description = it }
        dto.color?.let { matCategory.color = it }

        val saved = MaterialCategoryRepository.save(matCategory)
        logger.info("Material category updated successfully for categoryId=$id")
        return saved.id
    }

    fun deleteMaterialCategory(id: Long) {
        logger.info("Deleting material category with id=$id")
        var matCategory = MaterialCategoryRepository.findById(id).orElseThrow {
            logger.warn("Material category deletion failed: categoryId=$id not found")
            DuplicateResourceException("error.material_category.not_found")
        }

        matCategory.active = false
        MaterialCategoryRepository.save(matCategory)
        logger.info("Material category deleted successfully for categoryId=$id")
    }

    fun activateMaterialCategory(id: Long): Long{
        var matCategory = MaterialCategoryRepository.findById(id)
            .orElseThrow { DuplicateResourceException("Material category with id $id not found") }

        require(!matCategory.active) { "Material category with id $id is already active" }
        matCategory.active = true
        MaterialCategoryRepository.save(matCategory)
        return matCategory.id
    }






}