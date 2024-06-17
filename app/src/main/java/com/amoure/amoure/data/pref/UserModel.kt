package com.amoure.amoure.data.pref

data class UserModel(
    val accessToken: String,
    val userId: Int,
    val isLogin: Boolean = false,
    val role: String
)