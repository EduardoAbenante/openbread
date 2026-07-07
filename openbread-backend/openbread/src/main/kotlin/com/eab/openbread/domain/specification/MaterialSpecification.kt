package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.MaterialCategory
import com.eab.openbread.domain.model.RawMaterial
import org.springframework.data.jpa.domain.Specification

object MaterialSpecification {
    fun smartSearch(searchTerm: String?): Specification<RawMaterial> {
        return Specification<RawMaterial> { root, _, cb ->
            if (searchTerm.isNullOrBlank()) {
                cb.conjunction()
            } else {
                val queryLower = cb.literal("%${searchTerm.lowercase()}%")
                val pName = cb.like(cb.lower(root.get("name")), queryLower)
                val categoryId = searchTerm.toLongOrNull()
                if (categoryId != null) {
                    val pCategoryId = cb.equal(
                        root.get<MaterialCategory>("category").get<Long>("id"),
                        categoryId
                    )
                    cb.or(pName, pCategoryId)
                } else {
                    pName
                }
            }
        }
    }

    fun withActiveStatus(active: Boolean?): Specification<RawMaterial> {
        return Specification<RawMaterial> { root, _, cb ->
            if (active == null) cb.conjunction() else cb.equal(root.get<Boolean>("active"), active)
        }
    }
}