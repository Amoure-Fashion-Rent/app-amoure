package com.amoure.amoure.data.pref

data class UserModel(
    val accessToken: String,
    val isLogin: Boolean = false,
    val userType: String
)