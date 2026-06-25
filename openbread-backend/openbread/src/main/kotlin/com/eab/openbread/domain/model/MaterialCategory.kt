package com.eab.openbread.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table

enum class CategoryColor(val cssClass: String) {
    BLUE("bg-blue-500"),
    GREEN("bg-emerald-500"),
    RED("bg-rose-500"),
    YELLOW("bg-amber-500"),
    PURPLE("bg-violet-500"),
    GRAY("bg-slate-500")
}

@Entity
@Table(name = "MATERIAL_CATEGORIES")
class MaterialCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, length = 50)
    var name: String,

    @Column(length = 200)
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    var color: CategoryColor = CategoryColor.GRAY,

    var active: Boolean = true

): BaseAuditEntity()