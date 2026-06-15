package com.eab.openbread.domain.specification

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
                val pCategoryId = cb.equal(root.get<Long>("categoryId"), searchTerm.toLongOrNull())

                cb.or(pName, pCategoryId)
            }
        }
    }

    fun withActiveStatus(active: Boolean?): Specification<RawMaterial> {
        return Specification<RawMaterial> { root, _, cb ->
            if (active == null) cb.conjunction() else cb.equal(root.get<Boolean>("active"), active)
        }
    }
}