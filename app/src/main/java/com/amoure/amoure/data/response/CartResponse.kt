package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class CartResponse(

	@field:SerializedName("cart")
	val cart: Cart? = null
)

data class Cart(

	@field:SerializedName("delivery")
	val delivery: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("productId")
	val productId: List<String>? = null,

	@field:SerializedName("rentalDuration")
	val rentalDuration: Int? = null,

	@field:SerializedName("rentalStartDate")
	val rentalStartDate: String? = null,

	@field:SerializedName("rentalEndDate")
	val rentalEndDate: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deliveryPrice")
	val deliveryPrice: Int? = null,

	@field:SerializedName("paymentDate")
	val paymentDate: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
