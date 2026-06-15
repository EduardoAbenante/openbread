package com.eab.openbread.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 9, unique = true)
    val nif: String,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(nullable = false, length = 50)
    var surname: String,

    @Column(nullable = false, unique = true, length = 100)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(length = 20)
    var phone: String? = null,

    @Column(length = 10)
    var postalCode: String? = null,

    var photoUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    @Column(nullable = false)
    var active: Boolean = true
): BaseAuditEntity()

enum class Role {
    ADMIN, USER
}
