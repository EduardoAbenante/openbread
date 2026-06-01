package com.eab.openbread.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val nif: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val surname: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    val phone: String? = null,

    val postalCode: String? = null,

    val photoUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(nullable = false)
    val active: Boolean = true
)

enum class Role {
    ADMIN, USER
}
