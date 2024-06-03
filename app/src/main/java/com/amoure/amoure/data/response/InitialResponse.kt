package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class InitialResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
