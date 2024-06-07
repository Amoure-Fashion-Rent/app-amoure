package com.amoure.amoure.data.request

data class PutCartRequest (
    var delivery: String,
    var deliveryPrice: Int,
    var cardNumber: String,
    var cardExpiry: String,
    var cardCVV: String
)