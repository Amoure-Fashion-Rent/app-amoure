package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("userType")
	val userType: String,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("userId")
	val userId: String
)
