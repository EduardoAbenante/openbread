package com.eab.openbread.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table

@Entity
@Table(name = "MATERIAL_CATEGORIES")
class MaterialCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, length = 50)
    val name: String,

    @Column(length = 200)
    val description: String? = null,

    val active: Boolean = true

): BaseAuditEntity()