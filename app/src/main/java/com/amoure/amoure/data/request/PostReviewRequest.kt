package com.amoure.amoure.data.request

data class PostReviewRequest (
    var rating: Int,
    var comment: String,
    var createdAt: String,
)