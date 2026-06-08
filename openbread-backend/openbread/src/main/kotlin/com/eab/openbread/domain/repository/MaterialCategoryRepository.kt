package com.eab.openbread.domain.repository

import com.eab.openbread.domain.model.MaterialCategory
import org.springframework.data.jpa.repository.JpaRepository

interface MaterialCategoryRepository: JpaRepository<MaterialCategory, Long> {
}