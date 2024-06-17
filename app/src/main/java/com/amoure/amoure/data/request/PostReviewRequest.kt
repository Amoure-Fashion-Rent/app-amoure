package com.amoure.amoure.data.request

data class PostReviewRequest (
    var productId: Int,
    var rating: Int,
    var comment: String,
)