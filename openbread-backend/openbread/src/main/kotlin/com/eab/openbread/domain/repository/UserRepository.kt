package com.eab.openbread.domain.repository

import com.eab.openbread.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    fun existsByEmail(email: String): Boolean

    fun existsByNif(NIF: String): Boolean

    fun findByEmail(email: String): User?

    fun findByNif(NIF: String): User?
}