package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName


data class ErrorResponse(

	@field:SerializedName("field")
	val field: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
