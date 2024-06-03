package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null,
	// TODO
	@field:SerializedName("userType")
	val userType: String? = null
)
