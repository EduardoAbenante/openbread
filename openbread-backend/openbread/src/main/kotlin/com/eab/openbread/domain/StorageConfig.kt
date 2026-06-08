package com.eab.openbread.domain

enum class ImageProfile(
    val folderName: String,
    val maxWidth: Int,
    val maxHeight: Int,
    val quality: Double
    ) {
    AVATAR("avatar", 400, 400, 0.8),
    PRODUCT("product", 800, 800, 0.7),
    MATERIAL("material", 600, 600, 0.7),
    ALBARAN("documents", 1200, 1600, 0.5)
}