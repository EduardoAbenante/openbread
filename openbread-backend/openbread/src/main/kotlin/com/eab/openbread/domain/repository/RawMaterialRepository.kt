package com.eab.openbread.domain.repository

import com.eab.openbread.domain.model.RawMaterial
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RawMaterialRepository: JpaRepository<RawMaterial, Long>, JpaSpecificationExecutor<RawMaterial> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): RawMaterial?
    fun findByMaterialCategory(materialCategory: String): List<RawMaterial>
}