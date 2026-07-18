package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.MaterialCategory
import org.springframework.data.jpa.domain.Specification

object MaterialCategorySpecification {
    fun smartSearch(searchTerm: String?): Specification<MaterialCategory> {
        return Specification<MaterialCategory> { root, _, cb ->
            if (searchTerm.isNullOrBlank()) {
                cb.conjunction()
            } else {
                val queryLower = cb.literal("%${searchTerm.lowercase()}%")
                val pName = cb.like(cb.lower(root.get("name")), queryLower)
                cb.or(pName)
            }
        }
    }

    fun withActiveStatus(active: Boolean?): Specification<MaterialCategory> {
        return Specification<MaterialCategory> { root, _, cb ->
            if (active == null) {
                cb.conjunction()
            } else {
                cb.equal(root.get<Boolean>("active"), active)
            }
        }
    }
}