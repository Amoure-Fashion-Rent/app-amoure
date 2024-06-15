package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

	@field:SerializedName("ReviewResponse")
	val reviewResponse: List<ReviewItem?>? = null
)

data class ReviewItem(

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
