package com.amoure.amoure.data.request

data class PostCartRequest (
    var productId: Int,
    var productName: String,
    var productSize: String,
    var productColor: String,
    var rentPrice: Int,
    var deliveryMethod: String,
    var deliveryPrice: Int,
    var totalPrice: Int,
    var rentalStartDate: String,
    var rentalEndDate: String,
    var rentalDuration: Int = 4,
)