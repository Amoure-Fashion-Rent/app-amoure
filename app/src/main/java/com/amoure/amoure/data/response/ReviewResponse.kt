package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

	@field:SerializedName("reviews")
	val reviews: List<ReviewItem?>? = null
)

data class ReviewItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("user")
	val user: ReviewUser? = null,
)

data class ReviewUser(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,
)