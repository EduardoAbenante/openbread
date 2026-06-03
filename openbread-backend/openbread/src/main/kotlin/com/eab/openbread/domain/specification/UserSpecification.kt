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
                cb.conjunction() // Equivale a un "WHERE 1=1" (no filtra nada)
            } else {
                val searchTerm = "%${search.trim().lowercase()}%"

                // Creamos los predicados parciales (LIKE) para cada columna
                val pNif = cb.like(cb.lower(root.get("nif")), searchTerm)
                val pName = cb.like(cb.lower(root.get("name")), searchTerm)
                val pSurname = cb.like(cb.lower(root.get("surname")), searchTerm)
                val pEmail = cb.like(cb.lower(root.get("email")), searchTerm)
                val pPhone = cb.like(cb.lower(root.get("phone")), searchTerm)
                val pPostalCode = cb.like(cb.lower(root.get("postalCode")), searchTerm)

                // Los combinamos todos con un operador lógico OR
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