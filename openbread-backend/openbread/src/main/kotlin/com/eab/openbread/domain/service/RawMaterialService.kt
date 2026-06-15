package com.eab.openbread.domain.service

import com.eab.openbread.domain.ImageProfile
import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.domain.repository.MaterialCategoryRepository
import com.eab.openbread.domain.repository.RawMaterialRepository
import com.eab.openbread.domain.specification.MaterialSpecification
import com.eab.openbread.web.dto.material.MaterialCreateDTO
import com.eab.openbread.web.dto.material.MaterialResponseDTO
import com.eab.openbread.web.dto.material.MaterialUpdateDTO
import com.eab.openbread.web.dto.material.toDTO
import com.eab.openbread.web.dto.material.toEntity
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.domain.Specification
import org.springframework.web.multipart.MultipartFile

class RawMaterialService(
    private val materialRepository: RawMaterialRepository,
    private val materialCategoryRepository: MaterialCategoryRepository,
    private val mediaService: MediaService
) {
    private val logger = LoggerFactory.getLogger(RawMaterialService::class.java)

    //Create Read Update Delete

    fun createMaterial(materialDTO: MaterialCreateDTO): Long {
        logger.info("Creating material: ${materialDTO.name}")

        if (materialRepository.existsByName(materialDTO.name)) {
            logger.warn("Material creation failed: Name already in use (${materialDTO.name})")
            throw DuplicateResourceException("error.material.name_exists")
        }
        try {
            val newMaterial = materialDTO.toEntity()
            val savedMaterial = newMaterial
            logger.info("Material created successfully with id=${savedMaterial.id}")
            return savedMaterial.id
        } catch (e: DataIntegrityViolationException) {
            logger.error("Material creation failed due to database constraint violation", e)
            throw DuplicateResourceException("error.unexpected")
        }
    }

    fun getallMaterials(): List<MaterialResponseDTO> {
        logger.info("Fetching all raw materials")
        return materialRepository.findAll().map { toDTO(it) }
    }

    fun findMaterials(search: String?, active: Boolean?): List<MaterialResponseDTO> {
        logger.info("Searching users with Smart Search: query='$search', active=$active")

        val spec = Specification
            .where ( MaterialSpecification.smartSearch(search) )
            .and ( MaterialSpecification.withActiveStatus(active) )

        return materialRepository.findAll(spec).map { toDTO(it) }
    }

    fun updateMaterial(id: Long, updateDTO: MaterialUpdateDTO):  Long {
        logger.info("Updating raw material data for userId=$id")
        val material = materialRepository.findById(id)
            .orElseThrow {
                logger.warn("Material update failed: userId=$id not found")
                ResourceNotFoundException("error.material.not_found")
            }

        updateDTO.name?.let { material.name = it }
        updateDTO.description?.let { material.description = it }
        updateDTO.categoryId?.let {
            val cat = materialCategoryRepository.findById(it).orElseThrow{
                logger.info("Material update failed: categoryId=$it not found")
                ResourceNotFoundException("error.material.category_not_found")
            }
            material.category = cat
        }

        return materialRepository.save(material).id
    }

    fun updateUploadedPhoto(id: Long, file: MultipartFile): String {
        logger.info("Attempting to update photo for material with id=$id")
        val material = materialRepository.findById(id)
            .orElseThrow {
                logger.warn("Photo update failed: userId=$id not found")
                ResourceNotFoundException("error.material.not_found")
            }

        material.photoUrl?.let{ mediaService.deleteMedia(it)
        }

        val savedRelativePath = mediaService.processAndSaveImage(file, ImageProfile.MATERIAL)

        material.photoUrl = savedRelativePath
        materialRepository.save(material)

        logger.info("Photo updated successfully for material with id=$id")
        return savedRelativePath
    }

    fun deleteMaterial(id: Long) {
        logger.info("Deleting material with id=$id")

        val material = materialRepository.findById(id)
            .orElseThrow {
                logger.warn("Material deletion failed: userId=$id not found")
                ResourceNotFoundException("error.material.not_found")
            }

        require(material.active) { "Material with id $id is already active" }
        material.active = false

        materialRepository.save(material)
    }

    fun activateMaterial(id: Long): Long{
        val material = materialRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Material with id $id not found") }

        require(!material.active) { "Material with id $id is already active" }

        material.active = true
        materialRepository.save(material)

        return material.id
    }

}