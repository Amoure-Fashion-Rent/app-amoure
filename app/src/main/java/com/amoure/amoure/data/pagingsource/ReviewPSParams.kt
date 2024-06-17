package com.amoure.amoure.data.pagingsource

data class ReviewPSParams(
    val id: Int? = null,
    val productId: Int? = null,
    val page: Int? = null,
    val take: Int? = null,
    val includeUser: Boolean = true,
)