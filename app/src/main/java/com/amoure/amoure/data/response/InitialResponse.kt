package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class InitialResponse<T>(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: T? = null,
)
