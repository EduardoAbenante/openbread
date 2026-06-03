package com.eab.openbread.domain.specification

import com.eab.openbread.domain.model.User
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

/**
 * Builds a dynamic JPA Specification for filtering User entities based on the
 * provided optional parameters.
 *
 * This Specification allows flexible and composable search criteria:
 * - Each parameter is optional; only non-null values generate predicates.
 * - Text fields (name, surname) use case-insensitive partial matching (LIKE).
 * - Exact matching is applied for NIF, email, phone, postal code and active status.
 *
 * The resulting Specification is used by the repository to construct a dynamic
 * WHERE clause combining all active predicates with logical AND.
 *
 * @param nif Optional NIF filter (exact match).
 * @param name Optional name filter (case-insensitive partial match).
 * @param surname Optional surname filter (case-insensitive partial match).
 * @param email Optional email filter (exact match).
 * @param phone Optional phone filter (exact match).
 * @param postalCode Optional postal code filter (exact match).
 * @param active Optional active status filter.
 *
 * @return A Specification<User> representing the combined filtering criteria.
 */
object UserSpecification {
    fun build (
        nif: String?,
        name: String?,
        surname: String?,
        email: String?,
        phone: String?,
        postalCode: String?,
        active: Boolean?,
    ): Specification<User> {
        return Specification<User> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            nif?.let {predicates += cb.equal(root.get<String>("nif"), it)}
            name?.let{predicates += cb.like(cb.lower(root.get("name")), "%${it.lowercase()}")}
            surname?.let { predicates += cb.like(cb.lower(root.get("surname")), "%${it.lowercase()}%") }
            email?.let { predicates += cb.equal(root.get<String>("email"), it) }
            phone?.let { predicates += cb.equal(root.get<String>("phone"), it) }
            postalCode?.let { predicates += cb.equal(root.get<String>("postalCode"), it) }
            active?.let { predicates += cb.equal(root.get<Boolean>("active"), it) }

            cb.and(*predicates.toTypedArray())
        }
    }
}