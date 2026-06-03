package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.User
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object UserSpecification {

    /**
     * Variante SMART SEARCH: Crea una condición OR combinando todas las columnas de texto.
     * Convierte todo a minúsculas para que sea case-insensitive.
     */
    fun smartSearch(search: String?): Specification<User> {
        return Specification<User> { root, _, cb ->
            if (search.isNullOrBlank()) {
                cb.conjunction()
            } else {
                // Pasamos el patrón limpio a minúsculas, sin los % de Kotlin
                val queryLower = cb.literal("%${search.trim().lowercase()}%")

                val pNif = cb.like(cb.lower(root.get("nif")), queryLower)
                val pName = cb.like(cb.lower(root.get("name")), queryLower)
                val pSurname = cb.like(cb.lower(root.get("surname")), queryLower)
                val pEmail = cb.like(cb.lower(root.get("email")), queryLower)
                val pPhone = cb.like(cb.lower(root.get("phone")), queryLower)
                val pPostalCode = cb.like(cb.lower(root.get("postalCode")), queryLower)

                cb.or(pNif, pName, pSurname, pEmail, pPhone, pPostalCode)
            }
        }
    }

    /**
     * Filtro exclusivo para el estado (para poder combinar Smart Search + Estado)
     */
    fun withActiveStatus(active: Boolean?): Specification<User> {
        return Specification<User> { root, _, cb ->
            if (active == null) cb.conjunction() else cb.equal(root.get<Boolean>("active"), active)
        }
    }
}