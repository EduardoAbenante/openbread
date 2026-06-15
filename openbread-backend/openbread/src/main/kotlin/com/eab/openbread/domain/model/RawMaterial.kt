package com.eab.openbread.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "RAW_MATERIALS")
class RawMaterial(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, length = 50)
    var name: String,

    @Column(length = 200)
    var description: String? = null,

    var active: Boolean = true,

    var photoUrl: String? = null,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: MaterialCategory? = null
)
