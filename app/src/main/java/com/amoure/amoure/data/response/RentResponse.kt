package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class RentResponse(

	@field:SerializedName("rent")
	val rent: List<RentItem?>? = null
)

data class RentItem(

	@field:SerializedName("cartId")
	val cartId: String? = null,

	@field:SerializedName("userType")
	val userType: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
