package com.amoure.amoure.data.request

data class PostCartRequest (
    var productId: Int,
    var rentalStartDate: String,
    var rentalEndDate: String,
)