package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.web.dto.materialCategory.MaterialCategoryCreateDTO
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



}