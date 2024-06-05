package com.amoure.amoure.data.pref

data class UserModel(
    val accessToken: String,
    val userId: String,
    val isLogin: Boolean = false,
    val userType: String
)