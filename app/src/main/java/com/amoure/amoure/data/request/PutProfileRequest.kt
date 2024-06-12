package com.amoure.amoure.data.request

data class PutProfileRequest(
    var fullName: String,
    var email: String,
    var province: String,
    var city: String,
    var district: String,
    var postalCode: String,
    var addressDetail: String,
    var phoneNumber: String,
    var birthDate: String? = null,
)
