package com.amoure.amoure.data.request

data class PutCartRequest (
    var rentalStartDate: String,
    var rentalEndDate: String,
    var rentalDuration: Int,
    var delivery: String,
    var deliveryPrice: Int,
    var cardNumber: String,
    var cardExpiry: String,
    var cardCVV: String
)