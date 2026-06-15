package com.eab.openbread.domain.repository

import com.eab.openbread.domain.model.MaterialCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialCategoryRepository: JpaRepository<MaterialCategory, Long> {
    fun existsByName(name: String): Boolean
}