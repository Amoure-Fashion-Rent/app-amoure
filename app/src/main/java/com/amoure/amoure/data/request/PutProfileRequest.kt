package com.amoure.amoure.data.request

data class PutProfileRequest(
    var fullName: String,
    var province: String,
    var district: String,
    var postalCode: String,
    var addressDetail: String,
    var phoneNumber: String,
    var birthDate: String? = null,
)
