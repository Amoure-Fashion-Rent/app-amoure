package com.amoure.amoure.data.pagingsource

data class ProductPSParams(
    val id: Int? = null,
    val ownerId: Int? = null,
    val categoryId: Int? = null,
    val search: String? = null,
    val page: Int? = null,
    val take: Int? = null,
    val includeCategory: Boolean = true,
    val includeOwner: Boolean = true,
)
