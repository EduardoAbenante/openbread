package com.eab.openbread.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "USERS")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val NIF: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val surname: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    val phone: String?,

    val postalCode: String?,

    @Column(nullable = true)
    val photoUrl: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(nullable = false)
    val active : Boolean

)

enum class Role {
    ADMIN, USER
}